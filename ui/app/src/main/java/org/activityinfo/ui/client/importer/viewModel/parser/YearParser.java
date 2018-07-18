package org.activityinfo.ui.client.importer.viewModel.parser;

import com.google.gwt.regexp.shared.RegExp;
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
            double year = Double.parseDouble(value);
            return year >= 1000 && year <= 9999;
        } catch (Exception e) {
            return false;
        }
    }
}
