package org.activityinfo.ui.client.base.toolbar;

import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.List;

public class ToolbarBuilder {

    private List<VTree> actions = new ArrayList<>();

    public ToolbarBuilder action(VTree vTree) {
        actions.add(vTree);
        return this;
    }

    public ToolbarBuilder group(VTree... actions) {
        this.actions.add(H.div("toolbar__actions__group", actions));
        return this;
    }

    public VTree build() {
        return H.div("toolbar",
                H.div("toolbar__actions", actions.stream()));
    }
}
