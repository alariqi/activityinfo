package org.activityinfo.theme.client;

public enum Icon {
    HEADER_REPORTS, HEADER_NOTIFICATION, HEADER_SETTINGS, DATABASE, BUBBLE_ADD;

    public String href() {
        return "#" + name().toLowerCase();
    }
}
