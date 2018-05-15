package org.activityinfo.ui.client;

public enum Icon {
    HEADER_REPORTS,
    HEADER_NOTIFICATION,
    HEADER_SETTINGS,

    DATABASE,

    BUBBLE_ADD,
    BUBBLE_ARROWLEFT,
    BUBBLE_ARROWRIGHT,
    BUBBLE_CLOSE,
    BUBBLE_COLUMNS,

    EXPAND_DOWN,
    EXPAND_UP;

    public String href() {
        return "#" + name().toLowerCase();
    }
}
