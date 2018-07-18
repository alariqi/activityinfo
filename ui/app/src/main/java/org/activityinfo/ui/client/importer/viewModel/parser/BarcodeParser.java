package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class BarcodeParser implements FieldParser {
    @Override
    public double scoreContent(SourceColumn column) {
        return 1;
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return true;
    }
}
