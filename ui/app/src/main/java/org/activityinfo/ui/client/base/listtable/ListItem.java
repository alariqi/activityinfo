package org.activityinfo.ui.client.base.listtable;

import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.ui.client.Place2;
import org.activityinfo.ui.client.base.avatar.Avatar;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;

/**
 * Model for an item that appears in a navigation list view.
 */
public class ListItem {
    private String id;
    private String label;
    private SafeUri href;
    private Avatar avatar;
    private boolean availableOffline;

    public ListItem(String id, String label, SafeUri href, Avatar avatar, boolean availableOffline) {
        this.id = id;
        this.label = label;
        this.href = href;
        this.avatar = avatar;
        this.availableOffline = availableOffline;
    }

    public ListItem(String id, String label, Place2 place, Avatar avatar) {
        this.id = id;
        this.label = label;
        this.href = place.toUri();
        this.avatar = avatar;
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
        return ((GenericAvatar) avatar).href();
    }

    public Avatar getAvatar() {
        return avatar;
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
