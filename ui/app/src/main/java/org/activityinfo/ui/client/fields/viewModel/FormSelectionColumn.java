package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.model.resource.ResourceId;

import java.util.List;
import java.util.Optional;

public class FormSelectionColumn {

    private final int columnIndex;
    private List<FormSelectionItem> items;
    private Optional<ResourceId> selection;

    public FormSelectionColumn(int columnIndex, List<FormSelectionItem> items, Optional<ResourceId> selection) {
        this.columnIndex = columnIndex;
        this.items = items;
        this.selection = selection;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public List<FormSelectionItem> getItems() {
        return items;
    }

    public Optional<ResourceId> getSelection() {
        return selection;
    }

    public boolean isSelected(FormSelectionItem item) {
        return selection.map(s -> s.equals(item.getId())).orElse(false);
    }
}
