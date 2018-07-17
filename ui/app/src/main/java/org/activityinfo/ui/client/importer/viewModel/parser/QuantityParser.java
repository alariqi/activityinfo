package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public class QuantityParser implements FieldParser {
    @Override
    public double scoreContent(SourceColumn column) {
        return column.getNumberFraction();
    }
}
