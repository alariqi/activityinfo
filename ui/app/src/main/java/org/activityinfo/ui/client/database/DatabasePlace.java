package org.activityinfo.ui.client.database;

import com.google.gwt.place.shared.Place;
import org.activityinfo.model.resource.ResourceId;

import java.util.Objects;

public class DatabasePlace extends Place {

    private ResourceId databaseId;

    public DatabasePlace(ResourceId databaseId) {
        this.databaseId = databaseId;
    }

    public ResourceId getDatabaseId() {
        return databaseId;
    }

    @Override
    public String toString() {
        return "database/" + databaseId.asString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DatabasePlace that = (DatabasePlace) o;
        return Objects.equals(databaseId, that.databaseId);
    }

    @Override
    public int hashCode() {
        return databaseId.hashCode();
    }
}
