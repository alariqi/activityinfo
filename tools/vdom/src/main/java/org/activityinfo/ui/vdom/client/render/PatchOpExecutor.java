package org.activityinfo.ui.vdom.client.render;

import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.shared.diff.VPatchSet;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public interface PatchOpExecutor {

    Node updateProperties(Node Node, PropMap propPatch, PropMap previous);

    Node removeNode(VTree virtualNode, Node Node);

    Node insertNode(Node parentNode, VTree newNode);

    Node patchText(Node Node, String newText);

    Node replaceNode(VTree previousNode, VTree newNode, Node Node);

    Node patchComponent(Node Node, VComponent previous, VComponent replacement, VPatchSet patchSet);


}
