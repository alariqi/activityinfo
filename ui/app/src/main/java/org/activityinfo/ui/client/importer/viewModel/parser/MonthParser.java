package org.activityinfo.ui.client.importer.viewModel.parser;

import com.google.gwt.regexp.shared.RegExp;
import org.activityinfo.model.type.time.Month;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class MonthParser implements FieldParser {
    private static RegExp REGEX = RegExp.compile("^[0-9]{4}-[0-9]{1,2}");

    @Override
    public double scoreContent(SourceColumn column) {
        return column.scoreSample(REGEX);
    }

    @Override
    public boolean validate(@Nonnull String value) {
        try {
            Month.parseMonth(value);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
