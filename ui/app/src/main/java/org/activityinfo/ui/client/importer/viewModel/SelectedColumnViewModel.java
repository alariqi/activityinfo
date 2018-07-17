package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.targets.ColumnTarget;
import org.activityinfo.ui.client.importer.viewModel.targets.ColumnTargetSet;

import java.util.List;
import java.util.Optional;

public class SelectedColumnViewModel {

    private final SourceColumn source;
    private final List<ColumnTarget> targets;
    private final Optional<ColumnTarget> selected;

    public SelectedColumnViewModel(ColumnTargetSet targetSet, SourceColumn selectedColumn, FieldMappingSet mappings) {
        this.source = selectedColumn;
        this.targets = targetSet.getTargetsForColumn(selectedColumn);
        this.selected = this.targets.stream().filter(t -> t.isSelected(selectedColumn.getId(), mappings)).findAny();
    }

    public SourceColumn getSource() {
        return source;
    }

    public List<ColumnTarget> getTargets() {
        return targets;
    }

    public String getId() {
        return source.getId();
    }
}
