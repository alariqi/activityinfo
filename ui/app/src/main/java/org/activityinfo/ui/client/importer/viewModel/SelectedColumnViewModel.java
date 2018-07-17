package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModelSet;

import java.util.List;
import java.util.Optional;

public class SelectedColumnViewModel {

    private final SourceColumn source;
    private final List<ColumnTarget> targets;
    private final Optional<ColumnTarget> selected;
    private boolean ignored;

    public SelectedColumnViewModel(FieldViewModelSet fields, SourceColumn selectedColumn, FieldMappingSet mappings) {
        this.source = selectedColumn;
        this.targets = fields.getTargets();
        this.selected = this.targets.stream().filter(t -> t.isApplied(selectedColumn.getId(), mappings)).findAny();
        this.ignored = mappings.isIgnored(source.getId());
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

    public boolean isSelected(ColumnTarget target) {
        return selected.map(s -> s == target).orElse(false);
    }

    public boolean isIgnored() {
        return ignored;
    }
}
