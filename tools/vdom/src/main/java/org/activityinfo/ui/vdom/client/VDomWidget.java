package org.activityinfo.ui.vdom.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.vdom.client.render.DomBuilder;
import org.activityinfo.ui.vdom.client.render.DomPatcher;
import org.activityinfo.ui.vdom.client.render.RenderContext;
import org.activityinfo.ui.vdom.shared.diff.Diff;
import org.activityinfo.ui.vdom.shared.diff.VPatchSet;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.EventHandler;
import org.activityinfo.ui.vdom.shared.tree.VComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A GWT Widget that hosts a {@link org.activityinfo.ui.vdom.shared.tree.VTree}
 */
public class VDomWidget extends ComplexPanel implements RenderContext {

    private static final Logger LOGGER = Logger.getLogger(VDomWidget.class.getName());

    private VTree tree = new VNode(HtmlTag.DIV);

    private boolean updating = false;
    private boolean updateQueued = false;

    /**
     * Widgets that have been created as part of rendering a new or updated
     * tree but may not yet have been physically attached to the DOM tree.
     */
    private List<Widget> pendingAttachments = new ArrayList<>();
    private List<Widget> pendingDetachments = new ArrayList<>();

    private DomBuilder domBuilder;

    public VDomWidget() {
        domBuilder = new DomBuilder(this);
        setElement(Document.get().createDivElement());

        sinkEvents(Event.ONCLICK | Event.FOCUSEVENTS | Event.ONCHANGE);
    }


    /**
     * For a given real node, find the path, from the dom node down to this
     * event target.
     * 
     * @param currentEventTarget
     */
    private List<Integer> findParentPath(Node currentEventTarget) {
        List<Integer> path = new ArrayList<>();
        Node node = currentEventTarget;
        while(node != getElement()) {
            path.add(indexOf(node));
            node = node.getParentElement();
        }
        Collections.reverse(path);
        return path;
    }

    private int indexOf(Node node) {
        int index = 0;
        Node prev = node.getPreviousSibling();
        while(prev != null) {
            index ++;
            prev = prev.getPreviousSibling();
        }
        return index;
    }

    private List<VNode> findVNodeFromParentPath(List<Integer> path) {
        List<VNode> vPath = new ArrayList<>();
        VTree vParent = tree;
        for (Integer childIndex : path) {
            vParent = vParent.childAt(childIndex);
            while(vParent instanceof VComponent) {
                vParent = ((VComponent) vParent).vNode;
            }
            if(vParent instanceof VNode) {
                vPath.add(((VNode) vParent));
            }
        }
        Collections.reverse(vPath);
        return vPath;
    }

    public void update(VTree vTree) {
        assert !updating : "Update already in progress";
        try {
            updating = true;
            patchTree(vTree);
        } finally {
            updating = false;
            completeDetachments();
        }
    }

    /**
     * Re-renders the entire tree (with the exception of thunks) and
     * applies the differences to the DOM tree
     *
     * @param newTree
     */
    private void patchTree(VTree newTree) {
        VPatchSet diff = Diff.diff(tree, newTree);
        patch(diff);
        tree = newTree;
    }

    /**
     * Re-renders and diffs only those VThunks explicitly marked as dirty
     * during the last event loop
     */
    private void patchDirty() {
        patch(Diff.diff(tree, tree));
    }


    private void patch(VPatchSet diff) {

        DomPatcher domPatcher = new DomPatcher(domBuilder, this);
        Node rootNode = domPatcher.patch(getElement(), diff);

        if(rootNode != getElement()) {
            throw new IllegalStateException("Cannot replace the root node!");
        }
    }

    @Override
    public void attachWidget(Widget child, Element container) {
        this.add(child, container);
    }

    @Override
    public void detachWidget(Widget child) {
//        pendingDetachments.add(child);
        boolean wasChild = remove(child);// detach directly
        if (!wasChild) {
            throw new IllegalArgumentException("Widget is not child of this component.");
        }
    }

    @Override
    public void fireUpdate(VComponent component) {

        LOGGER.info("fireUpdate:" + component.debugId());

        if(!updateQueued) {
            updateQueued = true;
            Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    updateQueued = false;
                    patchDirty();
                }
            });
        }
    }


    @Override
    public void onBrowserEvent(Event event) {

        LOGGER.info("Event: " + event.getType());

        Element domNode = event.getEventTarget().cast();

        List<Integer> parentPath = findParentPath(domNode);
        List<VNode> vPath = findVNodeFromParentPath(parentPath);

        for (VNode vTree : vPath) {
            EventHandler eventHandler = vTree.properties.getEventHandler(event.getType());
            if(eventHandler != null) {
                eventHandler.onEvent(event);
                break;
            }
        }
    }

    private void completeDetachments() {
        for(Widget widget : pendingDetachments) {
            try {
                getChildren().remove(widget);
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "Exception while completing removal of " + widget, e);
            }
        }
        pendingDetachments.clear();
    }
}
