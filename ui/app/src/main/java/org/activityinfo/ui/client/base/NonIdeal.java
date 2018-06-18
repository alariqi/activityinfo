package org.activityinfo.ui.client.base;

import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.vdom.shared.html.SvgTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class NonIdeal {

    public static VTree empty() {
        return new VNode(SvgTag.SVG, new PropMap()
                .trustedSet("attributes",
                        new PropMap()
                                .set("viewBox", "0 0 21 17")
                                .set("preserveAspectRatio","xMinYMin meet"))
                    ,
                new VNode[] {
                        new VNode(SvgTag.USE, new PropMap()
                                .trustedSet("attributes", new PropMap().set("xlink:href", "#nis_emptystate")),
                                new VNode[0], "", Icon.SVG_NS)
                }, "",
                Icon.SVG_NS);
    }
}
