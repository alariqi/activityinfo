package org.activityinfo.ui.client.importer.viewModel.targets;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public class IgnoreColumn implements ColumnTarget {
    @Override
    public String getLabel() {
        return I18N.CONSTANTS.ignoreColumnAction();
    }

    @Override
    public boolean accept(SourceColumn column) {
        return true;
    }

    @Override
    public boolean isSelected(String columnId, FieldMappingSet mappings) {
        return !mappings.getColumnMapping(columnId).isPresent();
    }
}
