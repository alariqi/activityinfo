package org.activityinfo.ui.client.database;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.Place;

import java.util.Objects;
import java.util.Optional;

public class DatabasePlace extends Place {

    private ResourceId databaseId;
    private Optional<ResourceId> folderId;

    public DatabasePlace(ResourceId databaseId) {
        this.databaseId = databaseId;
        this.folderId = Optional.empty();
    }

    public DatabasePlace(ResourceId databaseId, ResourceId folderId) {
        this.databaseId = databaseId;
        this.folderId = Optional.of(folderId);
    }

    public ResourceId getDatabaseId() {
        return databaseId;
    }

    public Optional<ResourceId> getFolderId() {
        return folderId;
    }

    @Override
    public String toString() {
        if(folderId.isPresent()) {
            return "database/" + databaseId.asString() + "/" + folderId.get();
        } else {
            return "database/" + databaseId.asString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabasePlace that = (DatabasePlace) o;
        return Objects.equals(databaseId, that.databaseId) &&
                Objects.equals(folderId, that.folderId);
    }

    @Override
    public int hashCode() {
        return databaseId.hashCode();
    }
}
