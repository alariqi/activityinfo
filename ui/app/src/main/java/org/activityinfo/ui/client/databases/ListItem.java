package org.activityinfo.ui.client.databases;

import com.google.gwt.safehtml.shared.SafeUri;

/**
 * Model for an item that appears in a navigation list view.
 */
public class ListItem {
    private String id;
    private String label;
    private SafeUri href;
    private String iconHref;

    public ListItem(String id, String label, SafeUri href, String iconHref) {
        this.id = id;
        this.label = label;
        this.href = href;
        this.iconHref = iconHref;
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

    public String iconHref() {
        return iconHref;
    }

}
