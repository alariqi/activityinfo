package org.activityinfo.ui.client.base;

import com.google.common.base.Strings;
import org.activityinfo.ui.vdom.shared.html.SvgTag;
import org.activityinfo.ui.vdom.shared.tree.EventHandler;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;

public class Svg {

    private PropMap svgProps;
    private String href;

    public Svg() {
        svgProps = Props.create();
        svgProps.set("preserveAspectRatio", "xMinYMin meet");
    }

    public Svg setClasses(String classes) {
        svgProps.setClass(classes);
        return this;
    }

    public Svg maybeSetClasses(String classes) {
        if(!Strings.isNullOrEmpty(classes)) {
            setClasses(classes);
        }
        return this;
    }

    public Svg setHref(String href) {
        this.href = href;
        return this;
    }

    public Svg setViewBox(String viewBox) {
        svgProps.set("viewBox", viewBox);
        return this;
    }

    public Svg onclick(EventHandler eventHandler) {
        svgProps.onclick(eventHandler);
        return this;
    }

    public static VNode svg(String href) {
        return svg(null, href);
    }

    public static VNode svg(String classes, String href) {
        return svg(classes, href, "0 0 21 17");
    }

    public static VNode svg(String classes, String href, String viewBox) {
        return new Svg()
                .maybeSetClasses(classes)
                .setHref(href)
                .setViewBox(viewBox)
                .build();
    }

    public VNode build() {

        PropMap useProps = Props.create()
                .set("xlink:href", href);

        VNode useElement = new VNode(SvgTag.USE, useProps, VNode.NO_CHILDREN, "");

        return new VNode(SvgTag.SVG, svgProps, new VNode[] { useElement }, "");
    }

}
