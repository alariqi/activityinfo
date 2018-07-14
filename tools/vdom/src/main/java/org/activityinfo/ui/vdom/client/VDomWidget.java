package org.activityinfo.ui.vdom.client;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.vdom.shared.tree.VComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A GWT Widget that hosts a {@link org.activityinfo.ui.vdom.shared.tree.VTree}
 */
public class VDomWidget extends ComplexPanel implements RenderContext {

    private static final Logger LOGGER = Logger.getLogger(VDomWidget.class.getName());

    private VTree tree = null;

    private boolean updating = false;
    private boolean updateQueued = false;

    /**
     * Widgets that have been created as part of rendering a new or updated
     * tree but may not yet have been physically attached to the DOM tree.
     */
    private List<Widget> pendingAttachments = new ArrayList<>();
    private List<Widget> pendingDetachments = new ArrayList<>();

    public VDomWidget() {
        setElement(Document.get().createDivElement());
    }

    public void update(VTree vTree) {
        if(!updateQueued) {
            AnimationScheduler.get().requestAnimationFrame(timestamp -> doUpdate(vTree), getElement());
            updateQueued = true;
        }

        doUpdate(vTree);
    }

    private void doUpdate(VTree vTree) {

        updateQueued = false;

        assert !updating : "Update already in progress";
        try {
            updating = true;
            if(this.tree == null) {
                initialUpdate(vTree);
            } else {
                patchTree(vTree);
            }
        } finally {
            updating = false;
            completeDetachments();
        }
    }

    private void initialUpdate(VTree vTree) {
        Diff diff = new Diff(this);
        diff.init(getElement(), vTree);
        this.tree = vTree;
    }

    /**
     * Re-renders the entire tree and
     * applies the differences to the DOM tree
     *
     * @param newTree
     */
    private void patchTree(VTree newTree) {
        Diff diff = new Diff(this);
        diff.patch(getElement(), tree, newTree);
        tree = newTree;
    }

    /**
     * Re-renders and diffs only those VThunks explicitly marked as dirty
     * during the last event loop
     */
    private void patchDirty() {
        Diff diff = new Diff(this);
        diff.patch(getElement(), tree, tree);
    }


    @Override
    public void attachWidget(Widget child, Element container) {
        this.add(child, container);
    }

    @Override
    public void detachWidget(Widget child) {
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
            Scheduler.get().scheduleDeferred(() -> {
                updateQueued = false;
                patchDirty();
            });
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
