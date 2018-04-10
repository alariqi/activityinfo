package org.activityinfo.ui.client.folder;

import com.google.gwt.place.shared.Place;
import org.activityinfo.model.resource.ResourceId;

import java.util.Objects;

public class FolderPlace extends Place {

    private final ResourceId folderId;

    public FolderPlace(ResourceId folderId) {
        this.folderId = folderId;
    }

    public ResourceId getFolderId() {
        return folderId;
    }

    @Override
    public String toString() {
        return "folder/" + folderId.asString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FolderPlace that = (FolderPlace) o;
        return Objects.equals(folderId, that.folderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folderId);
    }
}
