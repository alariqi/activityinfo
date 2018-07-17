package org.activityinfo.ui.client.importer.state;

import org.activityinfo.model.formTree.LookupKey;

import java.util.Objects;

public class KeyMapping {
    private String key;
    private String columnId;

    public KeyMapping(String key, String columnId) {
        this.key = key;
        this.columnId = columnId;
    }

    public KeyMapping(LookupKey key, String columnId) {
        this.key = key.getFormId() + "." + key.getKeyField();
        this.columnId = columnId;
    }

    public String getKey() {
        return key;
    }

    public String getColumnId() {
        return columnId;
    }

    public boolean conflictsWith(KeyMapping other) {
        return this.columnId.equals(other.getColumnId()) ||
                this.key.equals(other.getKey());
    }

    @Override
    public String toString() {
        return "{" + key + "=>" + columnId + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyMapping that = (KeyMapping) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(columnId, that.columnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, columnId);
    }
}
