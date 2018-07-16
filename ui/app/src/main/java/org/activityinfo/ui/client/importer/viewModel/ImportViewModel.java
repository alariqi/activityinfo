package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.viewModel.targets.ColumnTargetSet;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.store.FormStore;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ImportViewModel {

    private final FormTree formTree;
    private final ColumnTargetSet targetSet;
    private final Observable<ImportState> state;
    private final Observable<ImportState.ImportStep> currentStep;
    private final Observable<SourceViewModel> source;
    private final Observable<String> selectedColumnId;
    private final Observable<FieldMappingSet> mapping;
    private final Observable<Optional<SelectedColumnViewModel>> columnTargetSelection;

    public static Observable<Maybe<ImportViewModel>> compute(FormStore formStore, Observable<ImportState> state) {

        Observable<ResourceId> formId = state.transform(s -> s.getFormId());
        Observable<Maybe<FormTree>> formTree = formId.join(id -> formStore.getFormTree(id).transform(tree -> tree.toMaybe()));

        return formTree.transform(maybe -> maybe.transform(tree -> new ImportViewModel(tree, state)));
    }

    public ImportViewModel(FormTree formTree, Observable<ImportState> state) {
        this.formTree = formTree;
        this.targetSet = new ColumnTargetSet(formTree);
        this.state = state;
        this.currentStep = state.transform(s -> s.getStep()).cache();
        this.source = state.transform(s -> s.getSource()).cache().transform(source -> {
            if(source.isPresent()) {
                return new SourceViewModel(source.get().getText());
            } else {
                return new SourceViewModel();
            }
        });
        this.selectedColumnId = Observable.transform(source, state.transform(s -> s.getSelectedColumnId()).cache(), (s, id) -> {
            if(s.hasColumnId(id)) {
                return id;
            } else if(!s.getColumns().isEmpty()) {
                return s.getColumns().get(0).getId();
            } else {
                return "";
            }
        });
        this.mapping = state.transform(s -> s.getMappings()).cache();
        this.columnTargetSelection = Observable.transform(source, selectedColumnId, mapping, (s, id, m) -> {
            return s.getColumnById(id).map(selectedColumn -> new SelectedColumnViewModel(targetSet, selectedColumn, m));
        });
    }

    public Observable<Boolean> isSourceValid() {
        return source.transform(s -> s.isValid());
    }

    public Observable<SourceViewModel> getSource() {
        return source;
    }

    public Observable<List<Breadcrumb>> getBreadcrumbs() {
        return Observable.just(Arrays.asList(Breadcrumb.DATABASES));
    }

    public Observable<String> getSelectedColumnId() {
        return selectedColumnId;
    }

    public Observable<ImportState.ImportStep> getCurrentStep() {
        return currentStep;
    }

    public String getFormLabel() {
        return formTree.getRootFormClass().getLabel();
    }

    public Observable<SelectedColumnViewModel> getSelectedColumnView() {
        return columnTargetSelection.transformIf(x -> com.google.common.base.Optional.fromJavaUtil(x));
    }
}
