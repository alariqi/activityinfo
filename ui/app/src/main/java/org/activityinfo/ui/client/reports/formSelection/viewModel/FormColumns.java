package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionState;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FormColumns {

    private final SelectionStatusMap selectionStatusMap;
    private final List<SelectionColumn> columns = new ArrayList<>();

    public FormColumns(SelectionStatusMap selectionStatusMap, FormSelectionState path) {
        this.selectionStatusMap = selectionStatusMap;

        SelectionTree tree = selectionStatusMap.getTree();
        SelectionNode parent = tree.getRoot();
        int depth = 0;

        for (ResourceId resourceId : path.getPath()) {
            columns.add(new SelectionColumn(depth, parent.getChildren(), Optional.of(resourceId)));
            parent = tree.getNode(resourceId);
            depth++;
        }

        if(!parent.isLeaf()) {
            columns.add(new SelectionColumn(depth, parent.getChildren(), Optional.empty()));
        }
    }

    public List<SelectionColumn> getColumns() {
        return columns;
    }

    public SelectionStatus isSelected(ResourceId id) {
        return selectionStatusMap.isSelected(id);
    }

    public static Observable<FormColumns> compute(FormStore formStore, Observable<Set<ResourceId>> selectedForms, Observable<FormSelectionState> path) {

        Observable<SelectionTree> selectionTree = formStore.getDatabases().transform(SelectionTree::new);
        Observable<SelectionStatusMap> selectionSet = org.activityinfo.observable.Observable.transform(selectionTree, selectedForms, SelectionStatusMap::new);
        return Observable.transform(selectionSet, path, FormColumns::new);
    }

}
