package org.activityinfo.ui.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

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
    EXPAND_UP;

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
}
