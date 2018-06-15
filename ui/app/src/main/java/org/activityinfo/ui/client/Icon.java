package org.activityinfo.ui.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.activityinfo.ui.vdom.shared.html.SvgTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public enum Icon {
    HEADER_REPORTS,
    HEADER_NOTIFICATION,
    HEADER_SETTINGS,

    DATABASE,

    BUBBLE_ADD,
    BUBBLE_ARROWLEFT,
    BUBBLE_ARROWRIGHT,
    BUBBLE_ATTACHMENT,
    BUBBLE_CLOSE,
    BUBBLE_CLOSE_ERROR,
    BUBBLE_CHECKMARK,
    BUBBLE_CHECKMARK_SUCCESS,
    BUBBLE_COLUMNS,
    BUBBLE_DOWN,

    DATE,

    OPTIONS,
    OPTIONS_HORIZONTAL,

    EXPAND_DOWN,
    EXPAND_UP,

    NIS_EMPTYSTATE;

    private static final String SVG_NS = "http://www.w3.org/2000/svg";

    public String href() {
        return "#" + name().toLowerCase();
    }


    public SafeHtml render(final int width, final int height) {
        return SafeHtmlUtils.fromTrustedString("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" +
                width + "\" height=\"" + height + "\"" +
                " viewBox=\"0 0 21 17\" class=\"icon\" preserveAspectRatio=\"xMinYMin meet\">" +
                "<use xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"" + href() + "\"></use></svg>");
    }
    public SafeHtml render() {
        return SafeHtmlUtils.fromTrustedString("<svg xmlns=\"http://www.w3.org/2000/svg\"" +
                " viewBox=\"0 0 21 17\" class=\"icon\" preserveAspectRatio=\"xMinYMin meet\">" +
                "<use xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"" + href() + "\"></use></svg>");
    }

    public VTree tree() {
        return new VNode(SvgTag.SVG, new PropMap()
                .trustedSet("attributes",
                        new PropMap()
                        .set("viewBox", "0 0 21 17")
                        .set("preserveAspectRatio","xMinYMin meet")
                        .set("class", "icon")),
                new VNode[] {
                    new VNode(SvgTag.USE, new PropMap()
                            .trustedSet("attributes", new PropMap().set("href", href())))
                }, "",
                SVG_NS);
    }
}
