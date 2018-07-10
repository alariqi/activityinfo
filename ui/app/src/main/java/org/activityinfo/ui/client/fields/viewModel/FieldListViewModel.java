package org.activityinfo.ui.client.fields.viewModel;

import java.util.Collections;
import java.util.List;

/**
 * The list of fields available to be used in this report or selection.
 */
public class FieldListViewModel {

    private final List<FieldListGroup> groups;

    public FieldListViewModel(List<FieldListItem> fields) {
        this.groups = Collections.singletonList(new FieldListGroup(fields));
    }

    public FieldListViewModel(FieldListGroup group) {
        this.groups = Collections.singletonList(group);
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
}
