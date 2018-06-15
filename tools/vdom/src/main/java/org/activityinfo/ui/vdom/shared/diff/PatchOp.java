package org.activityinfo.ui.vdom.shared.diff;

import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.client.render.PatchOpExecutor;


public interface PatchOp {

    Node apply(PatchOpExecutor executor, Node domNode);

}
