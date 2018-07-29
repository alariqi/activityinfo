package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnMatchMatrix;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModelSet;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.store.FormStore;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ImportViewModel {

    private final FormTree formTree;
    private final FieldViewModelSet fields;
    private final Observable<ImportState> state;
    private final Observable<ImportState.ImportStep> currentStep;
    private final Observable<SourceViewModel> source;
    private final Observable<ScoredSourceViewModel> scoredSource;
    private final Observable<String> selectedColumnId;
    private final Observable<MappedSourceViewModel> mappedSource;
    private final Observable<Optional<SelectedColumnViewModel>> selectedColumnView;
    private final Observable<ValidatedTable> validatedTable;
    private final Observable<ImportedTable> importedTable;

    public static Observable<Maybe<ImportViewModel>> compute(FormStore formStore, Observable<ImportState> state) {

        Observable<ResourceId> formId = state.transform(s -> s.getFormId()).cache();
        Observable<Maybe<FormTree>> formTree = formId.join(id -> formStore.getFormTree(id).transform(tree -> tree.toMaybe()));

        return formTree.transform(maybe -> maybe.transform(tree -> new ImportViewModel(formStore, tree, state)));
    }

    public ImportViewModel(FormStore formStore, FormTree formTree, Observable<ImportState> state) {
        this.formTree = formTree;
        this.fields = new FieldViewModelSet(formStore, formTree);
        this.state = state;
        this.currentStep = state.transform(s -> s.getStep()).cache();


        // Compute the SourceViewModel as a function of the text the user has pasted in
        // to the UI (or in the future, what has been uploaded)

        this.source = state.transform(s -> s.getSource()).cache().join(source -> {
            if(source.isPresent()) {
                return Observable.incremental(new SourceParser(source.get().getText()));
            } else {
                return Observable.just(new SourceViewModel());
            }
        });

        // Compute the effective column selection. The effective selection is also a function of the computed SourceViewModel,
        // because the source can still change after a selection has been made.

        this.selectedColumnId = Observable.transform(source, state.transform(s -> s.getSelectedColumnId()).cache(), (s, id) -> {
            if(s.hasColumnId(id)) {
                return id;
            } else if(!s.getColumns().isEmpty()) {
                return s.getColumns().get(0).getId();
            } else {
                return "";
            }
        });

        // Compute the column match matrix, which is a measure of similarity/compatibility between the imported
        // columns, and the destination form. This is an intermediate computation used to "guess" columns, but
        // we compute it separately because it is expensive and we only want to do it when the source changes.

        this.scoredSource = fields.scoreSource(source);

        // Using the user's explicit choices and the computed column match matrix as a back up, compute the
        // *effective* mappings between the imported columns and the target form.

        this.mappedSource = fields.guessMappings(scoredSource, state.transform(s -> s.getMappings()).cache());

        this.validatedTable = mappedSource.join(s -> s.getValidatedTable());

        this.importedTable = validatedTable.join(t -> t.getImportedTable());

        // Compute a viewModel of the current selected column that shows the current choice as well as alternatives
        // ranked by suitability.

        this.selectedColumnView = Observable.join(mappedSource, selectedColumnId, (s, id) -> {
            return s.getColumnById(id).transform(optionalColumn ->
                    optionalColumn.map(column ->
                        new SelectedColumnViewModel(s, column)));
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

    public Observable<MappedSourceViewModel> getMappedSource() {
        return mappedSource;
    }

    public Observable<ColumnMatchMatrix> getColumnMatrix() {
        return scoredSource.transform(s -> s.getMatchMatrix());
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

    public FormTree getFormTree() {
        return formTree;
    }

    public Observable<SelectedColumnViewModel> getSelectedColumnView() {
        return selectedColumnView.transformIf(x -> com.google.common.base.Optional.fromJavaUtil(x));
    }

    public FieldViewModelSet getFields() {
        return fields;
    }

    public Observable<ValidatedTable> getValidatedTable() {
        return validatedTable;
    }

    public Observable<ImportedTable> getImportedTable() {
        return importedTable;
    }

    public Observable<Boolean> isMappingComplete() {
        return mappedSource.transform(m -> m.isComplete()).cache();
    }


}
