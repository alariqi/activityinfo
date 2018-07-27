package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMapping;
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
    public String getRole() {
        return FieldMapping.VALUE_ROLE;
    }

    @Override
    public boolean isApplied(String columnId, FieldMappingSet mappings) {
        return mappings.isMapped(field.getName(),  FieldMapping.VALUE_ROLE, columnId);
    }

    @Override
    public boolean isApplied(FieldMappingSet mappings) {
        return mappings.isMapped(field.getName(), FieldMapping.VALUE_ROLE);
    }

    @Override
    public double scoreContent(SourceColumn column) {
        return parser.scoreContent(column);
    }

    @Override
    public FieldMappingSet apply(FieldMappingSet mappingSet, String columnId) {
        return mappingSet.withMapping(field.getName(), FieldMapping.VALUE_ROLE, columnId);
    }

    @Override
    public String toString() {
        return "{SimpleColumnTarget: " + field.getLabel() + ":" + field.getType().getTypeClass().getId() + "}";
    }
}
