package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceViewModel;
import org.activityinfo.ui.client.importer.viewModel.ScoredSourceViewModel;
import org.activityinfo.ui.client.importer.viewModel.SourceViewModel;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class FieldViewModelSet implements Iterable<FieldViewModel> {

    private static final Logger LOGGER = Logger.getLogger(FieldViewModelSet.class.getName());

    private List<FieldViewModel> fields = new ArrayList<>();
    private List<ColumnTarget> targets = new ArrayList<>();

    public FieldViewModelSet(FormStore formStore, FormTree formTree) {
        for (FormTree.Node node : formTree.getRootFields()) {
            FieldViewModelFactory.create(formStore, formTree, node.getField()).ifPresent(vm -> {
                fields.add(vm);
                targets.addAll(vm.getTargets());
            });
        }
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
    public Observable<MappedSourceViewModel> guessMappings(ScoredSourceViewModel sourceViewModel, FieldMappingSet explicitMappings) {

//        // Make a list of columns that need to be mapped
//        Set<String> explicitlyMappedColumns = explicitMappings.getMappedColumnIds();
//        List<SourceColumn> unmappedColumns = source.getColumns()
//                .stream()
//                .filter(c -> !c.isEmpty() && !explicitlyMappedColumns.contains(c.getId()))
//                .collect(toList());
//
//        // Make a list of binding targets that are still open
//        List<ColumnTarget> unusedTargets = new ArrayList<>();
//        for (FieldViewModel field : fields) {
//            unusedTargets.addAll(field.unusedTarget(explicitMappings));
//        }
//
//        // Score the columns based on names
//        ColumnMatchMatrix matchMatrix = new ColumnMatchMatrix(unmappedColumns, unusedTargets);
//
//        LOGGER.info("Column Match Matrix:\n" + matchMatrix.toCsv());

        return Observable.just(new MappedSourceViewModel(this, sourceViewModel, explicitMappings));
    }


    @Override
    public Iterator<FieldViewModel> iterator() {
        return fields.iterator();
    }
}
