package org.activityinfo.ui.client.fields.viewModel;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The list of fields available to be used in this report or selection.
 */
public class FieldListViewModel {

    private final Optional<String> draggingFieldId;

    private final List<FieldListGroup> groups;

    public FieldListViewModel(List<FieldListItem> fields) {
        this.groups = Collections.singletonList(new FieldListGroup(fields));
        draggingFieldId = Optional.empty();
    }

    public FieldListViewModel(FieldListViewModel list, Optional<String> draggingFieldId) {
        this.groups = list.getGroups();
        this.draggingFieldId = draggingFieldId;
    }

    public List<FieldListGroup> getGroups() {
        return groups;
    }

    public boolean isEmpty() {
        for (FieldListGroup group : groups) {
            if(!group.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Optional<String> getDraggingFieldId() {
        return draggingFieldId;
    }

    public boolean isDragging(FieldListItem item) {
        return draggingFieldId.map(d -> d.equals(item.getFormula())).orElse(false);
    }
}
