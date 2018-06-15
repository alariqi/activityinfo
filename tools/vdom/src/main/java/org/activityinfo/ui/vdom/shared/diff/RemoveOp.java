package org.activityinfo.ui.vdom.shared.diff;

import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.client.render.PatchOpExecutor;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class RemoveOp implements PatchOp {

    private VTree node;

    public RemoveOp(VTree node) {
        this.node = node;
    }

    @Override
    public Node apply(PatchOpExecutor executor, Node domNode) {
        return executor.removeNode(node, domNode);
    }

    @Override
    public String toString() {
        return "[REMOVE " + node + "]";
    }
}
