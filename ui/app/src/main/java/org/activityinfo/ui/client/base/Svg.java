package org.activityinfo.ui.client.base;

import com.google.common.base.Strings;
import org.activityinfo.ui.vdom.shared.html.SvgTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;

public class Svg {

    public static VNode svg(String href) {
        return svg(null, href);
    }

    public static VNode svg(String classes, String href) {
        return svg(classes, href, "0 0 21 17");
    }

    public static VNode svg(String classes, String href, String viewBox) {

        PropMap svgProps = Props.create()
                .set("viewBox", viewBox)
                .set("preserveAspectRatio", "xMinYMin meet");


        if(!Strings.isNullOrEmpty(classes)){
            svgProps.set("class", classes);
        }

        PropMap useProps = Props.create()
                .set("xlink:href", href);

        VNode useElement = new VNode(SvgTag.USE, useProps, VNode.NO_CHILDREN, "");

        return new VNode(SvgTag.SVG, svgProps, new VNode[] { useElement }, "");
    }
}
