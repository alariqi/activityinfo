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


}
