package org.activityinfo.theme.client;

public enum Icon {
    REPORTS, TASKS, SETTINGS, DATABASE, BUBBLE_ADD;

    public String href() {
        return "#" + name().toLowerCase();
    }
}
