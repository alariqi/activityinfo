package org.activityinfo.ui.client.analysis.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.FormMetadata;
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

    /**
     * Constructs a FieldListViewModel for a single form.
     */
    public static FieldListViewModel formFields(FormTree formTree) {
        List<SelectedFieldViewModel> fields = new ArrayList<>();

        for (FormTree.Node node : formTree.getRootFields()) {
            fields.add(new SelectedFieldViewModel(I18N.CONSTANTS.thisForm(), node.getField()));
        }
        for (FormMetadata form : formTree.getForms()) {
            if(!form.getId().equals(formTree.getRootFormId())) {
                if(form.isVisible()) {
                    for (FormField field : form.getFields()) {
                        fields.add(new SelectedFieldViewModel(form.getSchema().getLabel(), field));
                    }
                }
            }
        }

        return new FieldListViewModel(fields);
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
