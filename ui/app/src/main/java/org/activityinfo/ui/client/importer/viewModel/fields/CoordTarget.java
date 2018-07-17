package org.activityinfo.ui.client.importer.viewModel.fields;

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
    public boolean isSelected(String columnId, FieldMappingSet mappings) {
        return mappings.getColumnMapping(columnId).map(mapping -> {
            if(mapping instanceof GeoPointMapping) {
                GeoPointMapping geoPointMapping = (GeoPointMapping) mapping;
                if(geoPointMapping.getFieldName().equals(field.getId()) &&
                    geoPointMapping.isColumnMapped(columnId, axis)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    @Override
    public double scoreContent(SourceColumn column) {
        return column.getNumberFraction();
    }

    @Override
    public FieldMappingSet apply(FieldMappingSet mappingSet, String columnId) {
        return mappingSet.withGeoPointMapping(field.getName(), axis, columnId);
    }

}
