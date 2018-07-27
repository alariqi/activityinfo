package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.EventLoopScheduler;
import org.activityinfo.observable.IncrementalObservable;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;
import org.activityinfo.ui.client.importer.viewModel.parser.SimpleValidator;

import java.util.List;

import static java.util.Collections.singletonList;

public class SimpleMappedField implements MappedField {

    private final FormField field;
    private final SourceColumn sourceColumn;
    private final FieldParser parser;
    private final MappedSourceColumn mappedColumn;

    public SimpleMappedField(FormField field, SourceColumn sourceColumn, FieldParser parser) {
        this.field = field;
        this.sourceColumn = sourceColumn;
        this.parser = parser;
        this.mappedColumn = new MappedSourceColumn(sourceColumn, field.getLabel(),
                new IncrementalObservable<>(new SimpleValidator(sourceColumn.getColumnView(), parser), EventLoopScheduler.SCHEDULER));
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public Observable<FieldImporter> getImporter() {
        return Observable.just(new SimpleFieldImporter(field, sourceColumn.getColumnView(), parser));
    }

    @Override
    public FormField getField() {
        return field;
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        return singletonList(mappedColumn);
    }
}
