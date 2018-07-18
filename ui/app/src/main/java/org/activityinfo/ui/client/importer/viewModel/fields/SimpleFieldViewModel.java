package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceViewModel;
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
    public Optional<MappedField> mapColumns(SourceViewModel source, FieldMappingSet fieldMappingSet) {

        // First obtain the mapping for this field and its mapped column,
        // if they both exist.

        Optional<SourceColumn> sourceColumn = fieldMappingSet
                .getMappedValueColumn(this.field.getName())
                .flatMap(columnId -> source.getColumnById(columnId));

        // Now construct the mapper which validate and parse the source column
        return sourceColumn.map(column -> new SimpleMappedField(field, column, parser));
    }
}
