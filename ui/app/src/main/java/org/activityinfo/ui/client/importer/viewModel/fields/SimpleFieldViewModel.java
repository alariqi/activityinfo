package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMapping;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceViewModel;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SimpleFieldViewModel extends FieldViewModel {

    private final FormField field;
    private final FieldParser parser;

    public SimpleFieldViewModel(FormField field, FieldParser parser) {
        super(field);
        this.field = field;
        this.parser = parser;
    }

    @Override
    public List<ColumnTarget> getTargets() {
        return Collections.singletonList(new SimpleColumnTarget(field, parser));
    }

    @Override
    public Observable<Optional<ImportedFieldViewModel>> computeImport(Observable<MappedSourceViewModel> source) {
        return source.transform(s -> {
            return s.getFieldMappingSet().getSimpleFieldMapping(field.getName())
             .flatMap(m -> s.getSource().getColumnById(m.getSourceColumnId()))
             .map(c -> new SimpleImportedViewModel(c, parser));
        });
    }

    @Override
    public Optional<String> columnMappingLabel(FieldMapping fieldMapping, String columnId) {
        return Optional.of(field.getLabel());
    }
}
