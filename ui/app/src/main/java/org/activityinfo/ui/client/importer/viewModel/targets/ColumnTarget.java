package org.activityinfo.ui.client.importer.viewModel.targets;

import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SelectedColumnViewModel;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public interface ColumnTarget {

    String getLabel();

    boolean accept(SourceColumn column);

    boolean isSelected(String columnId, FieldMappingSet mappings);

    FieldMappingSet buildMapping(FieldMappingSet mappingSet, SelectedColumnViewModel column);
}
