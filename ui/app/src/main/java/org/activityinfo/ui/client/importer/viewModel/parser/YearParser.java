package org.activityinfo.ui.client.importer.viewModel.parser;

import com.google.gwt.regexp.shared.RegExp;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.time.YearValue;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class YearParser implements FieldParser {

    private static RegExp REGEX = RegExp.compile("^[0-9]{4}$");

    @Override
    public double scoreContent(SourceColumn column) {
        return column.scoreSample(REGEX);
    }

    @Override
    public boolean validate(@Nonnull String value) {
        try {
            int year = Integer.parseInt(value);
            return year >= 1000 && year <= 9999;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FieldValue parse(@Nonnull String value) {
        return new YearValue(Integer.parseInt(value));
    }
}
