package org.activityinfo.ui.vdom.shared.diff;

import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.client.render.PatchOpExecutor;
import org.activityinfo.ui.vdom.shared.tree.PropMap;

public class PatchPropsOp implements PatchOp {

    private final PropMap propDiff;
    private final PropMap previous;

    /**
     *
     * @param propDiff a {@code PropMap} of values that changed
     * @param previousValue the {@code PropMap} of previous values
     */
    public PatchPropsOp(PropMap propDiff, PropMap previousValue) {
        this.propDiff = propDiff;
        this.previous = previousValue;
    }

    @Override
    public Node apply(PatchOpExecutor executor, Node domNode) {
        return executor.updateProperties(domNode, propDiff, previous);
    }

    @Override
    public String toString() {
        return "[UPDATE PROPS " + propDiff + "]";
    }
}
