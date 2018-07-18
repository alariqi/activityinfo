package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.fields.BestColumnTargets;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnMatchMatrix;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;

import java.util.List;
import java.util.Optional;

public class SelectedColumnViewModel {

    private final ValidatedColumn column;
    private final Optional<ColumnTarget> selectedTarget;
    private final BestColumnTargets bestTargets;
    private boolean ignored;


    public SelectedColumnViewModel(MappedSourceViewModel source, ValidatedColumn column) {

        this.column = column;

        ColumnMatchMatrix columnMatrix = source.getColumnMatrix();
        FieldMappingSet fieldMappingSet = source.getFieldMappingSet();

        // Find the target that is currently selected
        this.selectedTarget = columnMatrix.getTargets().stream().filter(t -> t.isApplied(column.getId(), fieldMappingSet)).findAny();

        // Now find the targets that match by name (and are not excluded by content)
        bestTargets = columnMatrix.findBestTargets(column.getId());

        this.ignored = fieldMappingSet.isIgnored(column.getId());
    }


    public ValidatedColumn getColumn() {
        return column;
    }

    public List<ColumnTarget> getTargets() {
        return targets;
    }

    public String getId() {
        return column.getId();
    }

    public boolean isSelected(ColumnTarget target) {
        return selectedTarget.map(s -> s == target).orElse(false);
    }

    public boolean isIgnored() {
        return ignored;
    }

    public String getLabel() {
        return column.getColumn().getLabel();
    }
}
