package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
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

    public Observable<ColumnMatchMatrix> computeColumnMatchMatrix(Observable<SourceViewModel> source) {
        return source.transform(s -> new ColumnMatchMatrix(s.getColumns(), targets));
    }

    public Observable<FieldMappingSet> guessMappings(Observable<ColumnMatchMatrix> matrix, Observable<FieldMappingSet> mappings) {
        return Observable.join(matrix, mappings, this::guessMappings);
    }

    /**
     * Given the user's explict choices, make an educated guess about the remaining columns.
     */
    public Observable<FieldMappingSet> guessMappings(ColumnMatchMatrix matchMatrix, FieldMappingSet explicitMappings) {

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

        return Observable.just(explicitMappings);

    }

    @Override
    public Iterator<FieldViewModel> iterator() {
        return fields.iterator();
    }
}
