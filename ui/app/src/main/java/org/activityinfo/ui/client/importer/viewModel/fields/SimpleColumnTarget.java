package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;

public class SimpleColumnTarget implements ColumnTarget {
    private final FormField field;
    private final FieldParser parser;

    public SimpleColumnTarget(FormField field, FieldParser parser) {
        this.field = field;
        this.parser = parser;
    }

    @Override
    public String getLabel() {
        return field.getLabel();
    }

    @Override
    public boolean isSelected(String columnId, FieldMappingSet mappings) {
        return mappings.getColumnMapping(columnId)
                .map(m -> m.getFieldName().equals(field.getName()))
                .orElse(false);
    }

    @Override
    public double scoreContent(SourceColumn column) {
        return parser.scoreContent(column);
    }

    @Override
    public FieldMappingSet apply(FieldMappingSet mappingSet, String columnId) {
        return mappingSet.withSimpleMapping(field.getName(), columnId);
    }

    @Override
    public String toString() {
        return "{SimpleColumnTarget: " + field.getLabel() + ":" + field.getType().getTypeClass().getId() + "}";
    }
}
