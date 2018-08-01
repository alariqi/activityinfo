package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceViewModel;
import org.activityinfo.ui.client.importer.viewModel.ScoredSourceViewModel;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceViewModel;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FieldViewModelSet implements Iterable<FieldViewModel> {

    private static final Logger LOGGER = Logger.getLogger(FieldViewModelSet.class.getName());

    private ResourceId formId;
    private Optional<FieldViewModel> parentField;
    private List<FieldViewModel> fields = new ArrayList<>();
    private List<ColumnTarget> targets = new ArrayList<>();

    public FieldViewModelSet(FormStore formStore, FormTree formTree) {
        this.formId = formTree.getRootFormId();
        for (FormTree.Node node : formTree.getRootFields()) {
            FieldViewModelFactory.create(formStore, formTree, node.getField()).ifPresent(vm -> {
                fields.add(vm);
                targets.addAll(vm.getTargets());
            });
        }


    }

    public ResourceId getFormId() {
        return formId;
    }

    public List<ColumnTarget> getTargets() {
        return targets;
    }

    public Observable<ScoredSourceViewModel> scoreSource(Observable<SourceViewModel> source) {
        return source.transform(s -> new ScoredSourceViewModel(s, targets));
    }

    public Observable<MappedSourceViewModel> guessMappings(Observable<ScoredSourceViewModel> source, Observable<FieldMappingSet> mappings) {
        return Observable.join(source, mappings, this::guessMappings);
    }

    /**
     * Given the user's explicit choices, make an educated guess about the remaining columns.
     */
    public Observable<MappedSourceViewModel> guessMappings(ScoredSourceViewModel scoredSource, FieldMappingSet explicitMappings) {


        FieldMappingSet derivedMappings = explicitMappings.retain(scoredSource
                .getColumns()
                .stream()
                .map(SourceColumn::getId)
                .collect(Collectors.toSet()));

        // Find the set of columns that have not been explicitly mapped
        List<String> unmatchedColumns = new ArrayList<>();
        for (SourceColumn sourceColumn : scoredSource.getColumns()) {
            if (!explicitMappings.isColumnMapped(sourceColumn.getId()) &&
                !explicitMappings.isColumnIgnored(sourceColumn.getId())) {
                unmatchedColumns.add(sourceColumn.getId());
            }
        }

        // Find the targets that have not yet been assigned
        ColumnMatchMatrix matrix = scoredSource.getMatchMatrix();
        boolean[] unmatchedTargets = new boolean[matrix.getNumTargets()];
        for (int i = 0; i < matrix.getNumTargets(); i++) {
            unmatchedTargets[i] = !matrix.getTargets().get(i).isApplied(explicitMappings);
        }

        // For each unmatched column, find the best match
        for (String columnId : unmatchedColumns) {
            int targetIndex = matrix.findBestTarget(columnId, unmatchedTargets);
            if(targetIndex >= 0) {
                unmatchedTargets[targetIndex] = false;
                derivedMappings = matrix.getTargets().get(targetIndex).apply(derivedMappings, columnId);
            }
        }

        return Observable.just(new MappedSourceViewModel(this, scoredSource, derivedMappings));
    }


    @Override
    public Iterator<FieldViewModel> iterator() {
        return fields.iterator();
    }
}
