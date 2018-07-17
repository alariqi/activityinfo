package org.activityinfo.ui.client.importer.viewModel.targets;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SelectedColumnViewModel;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public class QuantityTarget implements ColumnTarget {
    private FormField field;

    public QuantityTarget(FormField field) {
        this.field = field;
    }

    @Override
    public String getLabel() {
        return field.getLabel();
    }

    @Override
    public boolean accept(SourceColumn column) {
        return column.hasNumbers();
    }

    @Override
    public boolean isSelected(String columnId, FieldMappingSet mappings) {
        return mappings.isSimpleMapped(field.getId(), columnId);
    }

    @Override
    public FieldMappingSet buildMapping(FieldMappingSet mappingSet, SelectedColumnViewModel column) {
        return mappingSet.replaceSimpleMapping(field.getName(), column.getId());
    }
}
