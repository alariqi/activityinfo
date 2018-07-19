package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class TextParser implements FieldParser {
    @Override
    public double scoreContent(SourceColumn column) {
        return ANYTHING_GOES_SCORE;
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return true;
    }
}
