package org.activityinfo.ui.client.importer.viewModel.targets;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SelectedColumnViewModel;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public class TextTarget implements ColumnTarget {

    private final FormField field;

    public TextTarget(FormField field) {
        this.field = field;
    }

    @Override
    public String getLabel() {
        return field.getLabel();
    }

    @Override
    public boolean accept(SourceColumn column) {
        return true;
    }

    @Override
    public boolean isSelected(String columnId, FieldMappingSet mappings) {
        return mappings.isSimpleMapped(field.getId(), columnId);
    }

    @Override
    public FieldMappingSet buildMapping(FieldMappingSet mappingSet, SelectedColumnViewModel column) {
        return mappingSet.replaceSimpleMapping(field.getName(), column.getSource().getId());
    }
}
