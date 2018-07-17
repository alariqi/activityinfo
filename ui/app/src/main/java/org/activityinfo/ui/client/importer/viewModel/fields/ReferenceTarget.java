package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.LookupKey;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.state.KeyMapping;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrixSet;

public class ReferenceTarget implements ColumnTarget {
    private final FormField field;
    private final LookupKey lookupKey;
    private final KeyMatrixSet keyMatrixSet;

    public ReferenceTarget(FormField field, LookupKey lookupKey, KeyMatrixSet keyMatrixSet) {
        this.field = field;
        this.lookupKey = lookupKey;
        this.keyMatrixSet = keyMatrixSet;
    }

    @Override
    public String getLabel() {
        return lookupKey.getKeyLabel();
    }

    @Override
    public boolean isSelected(String columnId, FieldMappingSet mappings) {
        return false;
    }

    @Override
    public double scoreContent(SourceColumn column) {
        return 1;
    }

    @Override
    public FieldMappingSet apply(FieldMappingSet mappingSet, String columnId) {
        return mappingSet.withReferenceMapping(field.getName(), new KeyMapping(lookupKey, columnId));
    }

    @Override
    public String toString() {
        return "{RefTarget " + lookupKey.getFormId() + "." + lookupKey.getKeyField() + ": " + getLabel() + "}";
    }
}