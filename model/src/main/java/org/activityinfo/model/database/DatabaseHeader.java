package org.activityinfo.model.database;

import org.activityinfo.model.resource.ResourceId;

public class DatabaseHeader {
    ResourceId databaseId;
    String label;
    String version;

    public DatabaseHeader(ResourceId databaseId, String label, String version) {
        this.databaseId = databaseId;
        this.label = label;
        this.version = version;
    }

    public ResourceId getDatabaseId() {
        return databaseId;
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }
}
