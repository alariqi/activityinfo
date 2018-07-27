package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.io.match.date.LatinDateParser;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class LocalDateParser implements FieldParser {

    private LatinDateParser parser = new LatinDateParser();

    @Override
    public double scoreContent(SourceColumn column) {
        return column.getDateFraction();
    }

    @Override
    public boolean validate(@Nonnull String value) {
        try {
            parser.parse(value);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FieldValue parse(@Nonnull String value) {
        return parser.parse(value);
    }
}
