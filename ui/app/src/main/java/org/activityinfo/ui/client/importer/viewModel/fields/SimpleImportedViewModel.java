package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;

public class SimpleImportedViewModel implements ImportedFieldViewModel {

    private final SourceColumn sourceColumn;
    private final FieldParser parser;

    public SimpleImportedViewModel(SourceColumn sourceColumn, FieldParser parser) {
        this.sourceColumn = sourceColumn;
        this.parser = parser;
    }
}
