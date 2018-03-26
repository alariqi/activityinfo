package org.activityinfo.theme.client;

/**
 * Model for an item that appears in a navigation list view.
 */
public class NavListItem {
    private String id;
    private String label;

    public NavListItem(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
