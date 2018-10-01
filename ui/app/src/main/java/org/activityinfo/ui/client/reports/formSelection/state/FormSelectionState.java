package org.activityinfo.ui.client.reports.formSelection.state;

import org.activityinfo.model.resource.ResourceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FormSelectionState {

    public static final ResourceId DATABASE_ROOT_ID = ResourceId.valueOf("databases");
    public static final ResourceId REPORTS_ROOT_ID = ResourceId.valueOf("reports");

    private final List<ResourceId> path;

    public FormSelectionState() {
        path = Collections.emptyList();
    }

    public FormSelectionState(List<ResourceId> path) {
        this.path = path;
    }

    public List<ResourceId> getPath() {
        return path;
    }

    /**
     * Creates a new, updated {@code FormSelectionState}, with the selection
     * in the n-th column changed to the given {@code resourceId}
     * @param columnIndex
     * @param resourceId
     * @return a new, updated model
     */
    public FormSelectionState navigateTo(int columnIndex, ResourceId resourceId) {

        // Is there a actually a change?
        if(columnIndex < path.size()) {
            if(path.get(columnIndex).equals(resourceId)) {
                return this;
            }
        }

        List<ResourceId> newPath = new ArrayList<>();
        for (int i = 0; i < columnIndex; i++) {
            newPath.add(path.get(i));
        }
        newPath.add(resourceId);

        return new FormSelectionState(newPath);
    }

    public Optional<ResourceId> getCurrent(int columnIndex) {
        if(columnIndex < path.size()) {
            return Optional.of(path.get(columnIndex));
        } else {
            return Optional.empty();
        }
    }

    public int length() {
        return path.size();
    }
}
