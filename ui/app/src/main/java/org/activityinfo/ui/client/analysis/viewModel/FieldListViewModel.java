package org.activityinfo.ui.client.analysis.viewModel;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.analysis.model.FormSelectionModel;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldListViewModel {


    private final List<SelectedFieldViewModel> fields;

    public FieldListViewModel(List<SelectedFieldViewModel> fields) {
        this.fields = fields;
    }

    public List<SelectedFieldViewModel> getFields() {
        return fields;
    }

    public static Observable<FieldListViewModel> compute(FormStore formStore, Observable<FormSelectionModel> model) {
        Observable<Set<ResourceId>> resourceSelection = model.transform(m -> m.getSelection()).cache();

        Observable<List<FormTree>> formTrees = resourceSelection.join(set ->
                Observable.flatten(set.stream()
                        .map(formStore::getFormTree)
                        .collect(Collectors.toList())));

        return formTrees.transform(FieldListViewModel::distinctFields);
    }



    private static FieldListViewModel distinctFields(List<FormTree> trees) {
        List<SelectedFieldViewModel> fields = new ArrayList<>();
        for (FormTree tree : trees) {
            if(tree.getRootState() == FormTree.State.VALID) {
                for (FormTree.Node node : tree.getRootFields()) {
                    fields.add(new SelectedFieldViewModel(node));
                }
            }
        }
        return new FieldListViewModel(fields);
    }
}
