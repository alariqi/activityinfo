package org.activityinfo.ui.client.importer.state;

import org.activityinfo.io.match.coord.CoordinateAxis;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class GeoPointMapping implements FieldMapping {
    private String fieldId;
    private Optional<String> latitudeColumnId;
    private Optional<String> longitudeColumnId;

    public GeoPointMapping(String targetFieldId, Optional<String> latitudeColumnId, Optional<String> longitudeColumnId) {
        this.fieldId = targetFieldId;
        this.latitudeColumnId = latitudeColumnId;
        this.longitudeColumnId = longitudeColumnId;
    }

    public GeoPointMapping(String fieldName, CoordinateAxis axis, String columnId) {
        this.fieldId = fieldName;
        if(axis == CoordinateAxis.LATITUDE) {
            this.latitudeColumnId = Optional.of(columnId);
            this.longitudeColumnId = Optional.empty();
        } else {
            this.latitudeColumnId = Optional.empty();
            this.longitudeColumnId = Optional.of(columnId);
        }
    }

    public GeoPointMapping(String fieldName, String latitudeColumnId, String longitudeColumnId) {
        this.fieldId = fieldName;
        this.latitudeColumnId = Optional.of(latitudeColumnId);
        this.longitudeColumnId = Optional.of(longitudeColumnId);
    }

    public Optional<String> getLatitudeColumnId() {
        return latitudeColumnId;
    }

    public Optional<String> getLongitudeColumnId() {
        return longitudeColumnId;
    }

    @Override
    public String getFieldName() {
        return fieldId;
    }

    @Override
    public Set<String> getMappedColumnIds() {
        Set<String> columns = new HashSet<>();
        latitudeColumnId.ifPresent(id -> columns.add(id));
        longitudeColumnId.ifPresent(id -> columns.add(id));
        return columns;
    }

    @Override
    public Optional<FieldMapping> withColumns(Predicate<String> columnPredicate) {
        Optional<String> latitude = latitudeColumnId.filter(columnPredicate);
        Optional<String> longitude = longitudeColumnId.filter(columnPredicate);
        if(latitude.isPresent() || longitude.isPresent()) {
            return Optional.of(new GeoPointMapping(fieldId, latitude, longitude));
        } else {
            return Optional.empty();
        }
    }

    public boolean isColumnMapped(String columnId, CoordinateAxis axis) {
        return false;
    }

    public GeoPointMapping withCoordMapping(CoordinateAxis axis, String columnId) {
        if(axis == CoordinateAxis.LATITUDE) {
            return new GeoPointMapping(fieldId, Optional.of(columnId), longitudeColumnId);
        } else {
            return new GeoPointMapping(fieldId, latitudeColumnId, Optional.of(columnId));
        }
    }

    @Override
    public String toString() {
        return "GeoPointMapping{" +
                 fieldId + " => (" +
                "lat: " + latitudeColumnId +
                ", lng: " + longitudeColumnId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPointMapping that = (GeoPointMapping) o;
        return Objects.equals(fieldId, that.fieldId) &&
                Objects.equals(latitudeColumnId, that.latitudeColumnId) &&
                Objects.equals(longitudeColumnId, that.longitudeColumnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId, latitudeColumnId, longitudeColumnId);
    }
}
