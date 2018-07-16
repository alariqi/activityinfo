package org.activityinfo.ui.client.importer.viewModel.targets;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.state.GeoPointMapping;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public class CoordTarget implements ColumnTarget {
    private FormField field;
    private CoordinateAxis axis;

    public CoordTarget(FormField field, CoordinateAxis axis) {
        this.field = field;
        this.axis = axis;
    }


    @Override
    public String getLabel() {
        return field.getLabel() + " " + axis.getLocalizedName();
    }

    @Override
    public boolean accept(SourceColumn column) {
        return true;
    }

    @Override
    public boolean isSelected(String columnId, FieldMappingSet mappings) {
        return mappings.getColumnMapping(columnId).map(mapping -> {
            if(mapping instanceof GeoPointMapping) {
                GeoPointMapping geoPointMapping = (GeoPointMapping) mapping;
                if(geoPointMapping.getFieldId().equals(field.getId()) &&
                    geoPointMapping.isColumnMapped(columnId, axis)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}
