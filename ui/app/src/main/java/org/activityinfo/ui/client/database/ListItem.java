package org.activityinfo.ui.client.database;

import com.google.gwt.safehtml.shared.SafeUri;

/**
 * Model for an item that appears in a navigation list view.
 */
public class ListItem {
    private String id;
    private String label;
    private SafeUri href;
    private String avatarHref;
    private boolean availableOffline;

    public ListItem(String id, String label, SafeUri href, String avatarHref, boolean availableOffline) {
        this.id = id;
        this.label = label;
        this.href = href;
        this.avatarHref = avatarHref;
        this.availableOffline = availableOffline;
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

    public String getAvatarHref() {
        return avatarHref;
    }


    public boolean isAvailableOffline() {
        return availableOffline;
    }

    public String getOfflineClass() {
        if(isAvailableOffline()) {
            return "page__item--offline";
        } else {
            return "";
        }
    }
}
