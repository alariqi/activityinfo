package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
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
    public boolean isApplied(String columnId, FieldMappingSet mappings) {
        return mappings.isMapped(field.getName(), axis.name(), columnId);
    }

    @Override
    public double scoreContent(SourceColumn column) {
        return column.getNumberFraction();
    }

    @Override
    public FieldMappingSet apply(FieldMappingSet mappingSet, String columnId) {
        return mappingSet.withMapping(field.getName(), axis.name(), columnId);
    }

}
