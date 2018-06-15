package org.activityinfo.ui.vdom.shared.diff;

import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.client.render.PatchOpExecutor;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import javax.annotation.Nonnull;

public class ReplaceOp implements PatchOp {

    @Nonnull
    private VTree previousNode;

    @Nonnull
    private final VTree newNode;

    public ReplaceOp(VTree previousNode, VTree newNode) {
        this.previousNode = previousNode;
        this.newNode = newNode;
    }

    @Override
    public Node apply(PatchOpExecutor executor, Node domNode) {
        return executor.replaceNode(previousNode, newNode, domNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplaceOp replaceOp = (ReplaceOp) o;

        if (!newNode.equals(replaceOp.newNode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return newNode.hashCode();
    }

    @Override
    public String toString() {
        return "REPLACE " + previousNode + " WITH " + newNode;
    }
}
