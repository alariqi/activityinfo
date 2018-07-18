package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;

import java.util.List;

import static java.util.Collections.singletonList;

public class SimpleMappedField implements MappedField {

    private final SourceColumn sourceColumn;
    private final FieldParser parser;
    private final MappedSourceColumn mappedColumn;

    public SimpleMappedField(FormField field, SourceColumn sourceColumn, FieldParser parser) {
        this.sourceColumn = sourceColumn;
        this.parser = parser;
        this.mappedColumn = new MappedSourceColumn(sourceColumn, field.getLabel());
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        return singletonList(mappedColumn);
    }
}
