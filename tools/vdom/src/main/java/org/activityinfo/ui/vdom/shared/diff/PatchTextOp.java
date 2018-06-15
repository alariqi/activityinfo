package org.activityinfo.ui.vdom.shared.diff;

import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.client.render.PatchOpExecutor;

public class PatchTextOp implements PatchOp {

    private final String text;

    public PatchTextOp(String text) {
        this.text = text;
    }

    @Override
    public Node apply(PatchOpExecutor executor, Node domNode) {
        return executor.patchText(domNode, text);
    }

    @Override
    public String toString() {
        return "[TEXT -> " + text + "]";
    }
}
