package org.activityinfo.ui.client.importer.state;

import java.util.Objects;

/**
 * Describes a mapping between a {@link FormField} and a source column using
 * a specific role.
 *
 * <p>For simple mappings, the role will always be "VALUE". For more complex mappings
 * such as those with GeoPoint or Reference fields, the role may refer to an axis such as "LATITUDE" or "LONGITUDE",
 * or to the name of key used to look up a referenced value.</p>
 *
 */
public class FieldMapping {

    public static final String VALUE_ROLE = "value";

    private String fieldName;
    private String role;
    private String columnId;

    public FieldMapping(String fieldName, String role, String columnId) {
        this.fieldName = fieldName;
        this.role = role;
        this.columnId = columnId;
    }

    public FieldMapping(String fieldName, String columnId) {
        this(fieldName, VALUE_ROLE, columnId);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getColumnId() {
        return columnId;
    }

    public String getRole() {
        return role;
    }

    boolean conflicts(FieldMapping other) {
        if(this.getFieldName().equals(other.getFieldName()) &&
           this.getRole().equals(other.getRole())) {
            return true;
        }
        if(this.getColumnId().equals(other.getColumnId())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "FieldMapping{" +
                "fieldName='" + fieldName + '\'' +
                ", role='" + role + '\'' +
                ", columnId='" + columnId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldMapping mapping = (FieldMapping) o;
        return Objects.equals(fieldName, mapping.fieldName) &&
                Objects.equals(role, mapping.role) &&
                Objects.equals(columnId, mapping.columnId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fieldName, role, columnId);
    }
}
