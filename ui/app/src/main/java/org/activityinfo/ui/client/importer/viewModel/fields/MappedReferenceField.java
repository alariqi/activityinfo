package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.LookupKey;
import org.activityinfo.observable.EventLoopScheduler;
import org.activityinfo.observable.IncrementalObservable;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MappedReferenceField implements MappedField {


    private final FormField field;
    private final KeyMatrix keyMatrix;
    private final LookupKey[] keys;
    private final SourceColumn[] columns;
    private final Observable<FieldImporter> importer;
    private final List<MappedSourceColumn> mappedColumns;

    public MappedReferenceField(FormField field, KeyMatrix keyMatrix, LookupKey[] keys, SourceColumn[] columns) {
        this.field = field;
        this.keyMatrix = keyMatrix;
        this.keys = keys;
        this.columns = columns;

        Observable<ReferenceMatcher.Result> matching = keyMatrix.index(Arrays.asList(keys)).join(
                index -> new IncrementalObservable<>(
                        new ReferenceMatcher(field, index, columns), EventLoopScheduler.SCHEDULER));

        importer = matching.transformIf(m -> m.getImporterIfDone());

        mappedColumns = new ArrayList<>();
        for (int i = 0; i < columns.length; i++) {
            int columnIndex = i;
            mappedColumns.add(new MappedSourceColumn(columns[i], keys[i].getKeyLabel(),
                    matching.transform(m -> m.getColumnValidation(columnIndex))));
        }
    }

    @Override
    public FormField getField() {
        return field;
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        return mappedColumns;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public Observable<FieldImporter> getImporter() {
        return importer;
    }
}
