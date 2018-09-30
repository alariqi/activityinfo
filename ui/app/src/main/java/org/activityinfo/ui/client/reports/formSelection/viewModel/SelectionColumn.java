package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.model.resource.ResourceId;

import java.util.List;
import java.util.Optional;

/**
 * A column in the selection tree.
 *
 * <p>This basically includes all the nodes at a given depth in the tree.
 *
 * <p>Each node has a {@link SelectionStatus}, but each column has one active node. The active node
 * has its children displayed in the following column.</p>
 */
public class SelectionColumn {

    private final int columnIndex;
    private List<SelectionNode> items;
    private Optional<ResourceId> selection;

    public SelectionColumn(int columnIndex, List<SelectionNode> items, Optional<ResourceId> activeNodeId) {
        this.columnIndex = columnIndex;
        this.items = items;
        this.selection = activeNodeId;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public List<SelectionNode> getItems() {
        return items;
    }

    public Optional<ResourceId> getActiveNodeId() {
        return selection;
    }

    public boolean isActive(SelectionNode item) {
        return selection.map(s -> s.equals(item.getId())).orElse(false);
    }
}
