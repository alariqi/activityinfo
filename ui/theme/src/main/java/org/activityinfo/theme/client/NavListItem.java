package org.activityinfo.theme.client;

import com.google.gwt.safehtml.shared.SafeUri;

/**
 * Model for an item that appears in a navigation list view.
 */
public class NavListItem {
    private String id;
    private String label;
    private SafeUri href;

    public NavListItem(String id, String label, SafeUri href) {
        this.id = id;
        this.label = label;
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public SafeUri getHref() {
        return href;
    }
}
