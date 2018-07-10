package org.activityinfo.ui.client;

import org.activityinfo.ui.vdom.shared.tree.VTree;

/**
 * A standalone page
 */
public abstract class Page {

    public abstract VTree render();

    public boolean tryNavigate(Place place) {
        return false;
    }

    /**
     * Called when the window is about to close, or when we are about to navigate away.
     */
    public String mayStop() {
        return null;
    }
}
