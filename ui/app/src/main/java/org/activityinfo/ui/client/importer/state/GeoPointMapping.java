package org.activityinfo.ui.client.importer.state;

import com.google.common.collect.Sets;
import org.activityinfo.io.match.coord.CoordinateAxis;

import java.util.Set;

public class GeoPointMapping implements FieldMapping {
    private String fieldId;
    private String latitudeColumnId;
    private String longitudeColumnId;

    public GeoPointMapping(String targetFieldId, String latitudeColumnId, String longitudeColumnId) {
        this.fieldId = targetFieldId;
        this.latitudeColumnId = latitudeColumnId;
        this.longitudeColumnId = longitudeColumnId;
    }

    public String getLatitudeColumnId() {
        return latitudeColumnId;
    }

    public String getLongitudeColumnId() {
        return longitudeColumnId;
    }

    @Override
    public String getFieldId() {
        return fieldId;
    }

    @Override
    public Set<String> getMappedColumnIds() {
        return Sets.newHashSet(latitudeColumnId, longitudeColumnId);
    }

    public boolean isColumnMapped(String columnId, CoordinateAxis axis) {
        return false;
    }
}
