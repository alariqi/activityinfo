/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.observable;

import com.google.gwt.core.client.Scheduler;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An observable that is a function of a tree of other observables.
 *
 * <p>To use this class, you must provide a {@link TreeLoader} implementation.</p>
 *
 * @param <KeyT> the type of a key that uniquely identifies tree nodes
 * @param <NodeT> the type of the tree nodes.
 * @param <TreeT> the type of the final tree constructed from the nodes.
 */
public final class ObservableTree<KeyT, NodeT, TreeT> extends Observable<TreeT> {

    private static final Logger LOGGER = Logger.getLogger(ObservableTree.class.getName());

    public interface TreeLoader<KeyT, NodeT, TreeT> {

        /**
         *
         * @return the key of this tree's root node.
         */
        KeyT getRootKey();

        /**
         * @return the value of the node identified by {@code nodeKey}
         */
        Observable<NodeT> get(KeyT nodeKey);

        /**
         *
         * @return the keys of the given {@code node}'s children.
         */
        Iterable<KeyT> getChildren(NodeT node);

        /**
         * Construct a tree from the set of loaded nodes.
         * @param nodes the nodes discovered in this tree. All nodes will be loaded when this method is called.
         * @return a new tree structure.
         */
        TreeT build(Map<KeyT, Observable<NodeT>> nodes);
    }

    private final TreeLoader<KeyT, NodeT, TreeT> loader;
    private final Scheduler scheduler;

    private Map<KeyT, Observable<NodeT>> nodes = new HashMap<>();
    private Map<KeyT, Subscription> subscriptions = new HashMap<>();

    private TreeT value;

    private boolean crawling = false;
    private boolean crawlPending = false;

    public ObservableTree(TreeLoader<KeyT, NodeT, TreeT> loader, Scheduler scheduler) {
        this.loader = loader;
        this.scheduler = scheduler;
    }

    @Override
    public boolean isLoading() {
        return value == null;
    }

    @Override
    public TreeT get() {
        assert value != null : "Tree is not loaded!";
        return value;
    }

    @Override
    protected void onConnect() {
        connectTo(loader.getRootKey());
        recrawl();
    }

    @Override
    protected void onDisconnect() {
        this.value = null;
        for (Subscription subscription : subscriptions.values()) {
            subscription.unsubscribe();
        }
        nodes.clear();
        subscriptions.clear();
    }

    private Observable<NodeT> connectTo(KeyT nodeKey) {
        Observable<NodeT> node = nodes.get(nodeKey);
        if(node == null) {
            node = loader.get(nodeKey);
            Subscription subscription = node.subscribe(new Observer<NodeT>() {

                private boolean onConnect = true;

                @Override
                public void onChange(Observable<NodeT> formClass) {
                    if(onConnect) {
                        onConnect = false;
                    } else {
                        ObservableTree.this.onNodeChanged(formClass);
                    }
                }
            });

            nodes.put(nodeKey, node);
            subscriptions.put(nodeKey, subscription);
        }
        return node;
    }

    private void disconnectFrom(KeyT nodeKey) {
        nodes.remove(nodeKey);
        Subscription subscription = subscriptions.remove(nodeKey);
        subscription.unsubscribe();
    }

    private void onNodeChanged(Observable<NodeT> formClass) {
        if(formClass.isLoaded()) {
            if(crawling) {
                crawlPending = true;
            } else {
                recrawl();
            }
        }
    }

    /**
     * Crawl from the root tree to all the leaves to find the set
     * of forms we need to query,
     */
    private void recrawl() {

        LOGGER.info("Tree " + loader + ": Recrawl starting...");

        Set<KeyT> reachable = new HashSet<>();
        Set<KeyT> loading = new HashSet<>();

        crawling = true;
        value = null;

        try {

            crawl(loader.getRootKey(), reachable, loading);

            // First clean up forms that are no longer reachable
            List<KeyT> connectedForms = new ArrayList<>(nodes.keySet());
            for (KeyT nodeKey : connectedForms) {
                if (!reachable.contains(nodeKey)) {
                    disconnectFrom(nodeKey);
                }
            }

            LOGGER.info("Tree " + loader + ": reachable = " + reachable +
                    ", loading = " + loading);


            // Otherwise if we've got everything, we can build the tree
            if (loading.isEmpty()) {
                rebuildTree();
            }

            // Otherwise we have to wait for one of our pending nodes to load
            // and then we can recrawl.

        } finally {
            crawling = false;
        }

        // Was there a change to one of the nodes while we crawling?
        // Restart a crawl now

        if(crawlPending) {
            scheduler.scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    ObservableTree.this.recrawl();
                }
            });
            crawlPending = false;
        }
    }

    /**
     * Recursively search the tree of forms for those that are reachable, missing, and still loading.
     */
    private void crawl(KeyT parentKey, Set<KeyT> reachable, Set<KeyT> loading) {
        boolean seenForFirstTime = reachable.add(parentKey);

        if(!seenForFirstTime) {
            return;
        }

        Observable<NodeT> node = connectTo(parentKey);
        if(node == null) {
            node = connectTo(parentKey);
        }

        if(node.isLoading()) {
            loading.add(parentKey);

        } else if(node.isLoaded()) {

            for (KeyT childKey : loader.getChildren(node.get())) {
                crawl(childKey, reachable, loading);
            }
        }
    }

    /**
     * After we have a loaded copy of all the form schemas, build the form tree and fire listeners.
     */
    private void rebuildTree() {

        LOGGER.info("Tree " + loader + " complete!");

        try {

            this.value = loader.build(nodes);

            fireChange();
        } catch (Error e) {
            LOGGER.log(Level.SEVERE, "Exception rebuilding tree", e);
        }
    }

}
