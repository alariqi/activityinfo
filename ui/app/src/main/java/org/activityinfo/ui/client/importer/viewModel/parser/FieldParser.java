package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public interface FieldParser {
    double scoreContent(SourceColumn column);

    boolean validate(@Nonnull String value);
}
