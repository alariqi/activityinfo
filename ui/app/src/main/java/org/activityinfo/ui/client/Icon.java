package org.activityinfo.ui.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public enum Icon {
    HEADER_DATABASES,
    HEADER_REPORTS,
    HEADER_NOTIFICATION,
    HEADER_SETTINGS,

    DATABASE,

    BUBBLE_ADD,
    BUBBLE_ARROWLEFT,
    BUBBLE_ARROWRIGHT,
    BUBBLE_ATTACHMENT,
    BUBBLE_EXPORT,
    BUBBLE_IMPORT,
    BUBBLE_CLOSE,
    BUBBLE_CLOSE_ERROR,
    BUBBLE_CHECKMARK,
    BUBBLE_CHECKMARK_SUCCESS,
    BUBBLE_COLUMNS,
    BUBBLE_DOWN,

    CHECKMARK,
    CLOSE_WHITE,

    DATE,

    OPTIONS,
    OPTIONS_HORIZONTAL,

    EXPAND_DOWN,
    EXPAND_UP,

    NIS_EMPTYSTATE, BUBBLE_EDIT, BUBBLE_USER, BUBBLE_FULLSCREEN, BUBBLE_SUBFORM;

    public static final String SVG_NS = "http://www.w3.org/2000/svg";

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
        return Svg.svg("icon", href(), "0 0 21 17");
    }
}