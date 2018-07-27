package org.activityinfo.ui.client.importer.viewModel.parser;

import com.google.gwt.regexp.shared.RegExp;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.time.EpiWeek;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class WeekParser implements FieldParser {

    private static RegExp REGEX = RegExp.compile("^[0-9]{4}[A-Z][0-9]{1,2}$");

    @Override
    public double scoreContent(SourceColumn column) {
        return column.scoreSample(REGEX);
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return true;
    }

    @Override
    public FieldValue parse(@Nonnull String value) {
        return EpiWeek.parse(value);
    }

}
