package org.activityinfo.ui.client.base;

import com.google.common.base.Strings;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.vdom.shared.html.SvgTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class NonIdeal {

    public static VTree empty() {
        return svg("","#nis_emptystate");
    }

    public static VNode svg(String href) {
        return svg(null, href);
    }

    public static VNode svg(String classes, String href) {
        PropMap svgProps = new PropMap()
                .trustedSet("attributes",
                        new PropMap()
                                .set("viewBox", "0 0 21 17")
                                .set("preserveAspectRatio", "xMinYMin meet"));

        if(!Strings.isNullOrEmpty(classes)){
            svgProps.setClass(classes);
        }

        PropMap useProps = new PropMap()
                .trustedSet("attributes", new PropMap().set("xlink:href", href));

        VNode useElement = new VNode(SvgTag.USE, useProps, VNode.NO_CHILDREN, "", Icon.SVG_NS);

        return new VNode(SvgTag.SVG, svgProps, new VNode[] { useElement }, "", Icon.SVG_NS);
    }
}
