package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public interface FieldParser {

    /**
     * The score to assign for fields which basically accept any input. By assigning a score
     * less than zero, fields which actually match well, like dates or numbers, will rank higher.
     */
    double ANYTHING_GOES_SCORE = 0.75;

    double scoreContent(SourceColumn column);

    boolean validate(@Nonnull String value);
}
