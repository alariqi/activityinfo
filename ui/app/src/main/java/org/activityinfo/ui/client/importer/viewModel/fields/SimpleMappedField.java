package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.IncrementalObservable;
import org.activityinfo.observable.Scheduler;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;
import org.activityinfo.ui.client.importer.viewModel.parser.SimpleValidator;

import java.util.List;

import static java.util.Collections.singletonList;

public class SimpleMappedField implements MappedField {

    private final SourceColumn sourceColumn;
    private final FieldParser parser;
    private final MappedSourceColumn mappedColumn;

    public SimpleMappedField(FormField field, SourceColumn sourceColumn, FieldParser parser) {
        this.sourceColumn = sourceColumn;
        this.parser = parser;
        this.mappedColumn = new MappedSourceColumn(sourceColumn, field.getLabel(),
                new IncrementalObservable<>(new SimpleValidator(sourceColumn.getColumnView(), parser),
                        new Scheduler() {
                            @Override
                            public void schedule(Runnable runnable) {
                                com.google.gwt.core.client.Scheduler.get().scheduleFinally(new com.google.gwt.core.client.Scheduler.ScheduledCommand() {
                                    @Override
                                    public void execute() {
                                        runnable.run();
                                    }
                                });
                            }
                        }));
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        return singletonList(mappedColumn);
    }
}
