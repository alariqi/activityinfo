package org.activityinfo.ui.client.reports.formSelection.state;

import org.activityinfo.model.resource.ResourceId;

import java.util.*;

public class FormSelectionState {

    public static final ResourceId DATABASE_ROOT_ID = ResourceId.valueOf("databases");
    public static final ResourceId REPORTS_ROOT_ID = ResourceId.valueOf("reports");

    private final List<ResourceId> navigationPath;
    private final Set<ResourceId> selection;

    public FormSelectionState() {
        navigationPath = Collections.emptyList();
        selection = Collections.emptySet();
    }

    public FormSelectionState(List<ResourceId> navigationPath, Set<ResourceId> selection) {
        this.navigationPath = navigationPath;
        this.selection = selection;
    }

    public List<ResourceId> getNavigationPath() {
        return navigationPath;
    }

    /**
     * Creates a new, updated {@code FormSelectionModel}, with the selection
     * in the n-th column changed to the given {@code resourceId}
     * @param columnIndex
     * @param resourceId
     * @return a new, updated model
     */
    public FormSelectionState navigateTo(int columnIndex, ResourceId resourceId) {

        // Is there a actually a change?
        if(columnIndex < navigationPath.size()) {
            if(navigationPath.get(columnIndex).equals(resourceId)) {
                return this;
            }
        }

        List<ResourceId> newPath = new ArrayList<>();
        for (int i = 0; i < columnIndex; i++) {
            newPath.add(navigationPath.get(i));
        }
        newPath.add(resourceId);

        return new FormSelectionState(newPath, selection);
    }

    public FormSelectionState toggleSelection(ResourceId id) {
        Set<ResourceId> newSelection = new HashSet<>(selection);
        if(newSelection.contains(id)) {
            newSelection.remove(id);
        } else {
            newSelection.add(id);
        }

        return new FormSelectionState(navigationPath, newSelection);
    }

    public Set<ResourceId> getSelection() {
        return selection;
    }

    public boolean isSelected(ResourceId resourceId) {
        return selection.contains(resourceId);
    }

    public Optional<ResourceId> getCurrent(int columnIndex) {
        if(columnIndex < navigationPath.size()) {
            return Optional.of(navigationPath.get(columnIndex));
        } else {
            return Optional.empty();
        }
    }
}
