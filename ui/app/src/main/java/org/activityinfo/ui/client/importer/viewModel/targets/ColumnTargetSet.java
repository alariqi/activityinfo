package org.activityinfo.ui.client.importer.viewModel.targets;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Set of targets to which columns can be mapped.
 *
 * This is a function of the FormTree.
 */
public class ColumnTargetSet {
    private List<ColumnTarget> targets;

    public ColumnTargetSet(FormTree formTree) {
        this.targets = new ArrayList<>();
        for (FormTree.Node node : formTree.getRootFields()) {
            targets.addAll(ColumnTargetFactory.create(node.getField()));
        }
        targets.add(new IgnoreColumn());
    }

    public List<ColumnTarget> getTargets() {
        return targets;
    }

    public List<ColumnTarget> getTargetsForColumn(SourceColumn column) {
        return targets.stream().filter(target -> target.accept(column)).collect(Collectors.toList());
    }
}
