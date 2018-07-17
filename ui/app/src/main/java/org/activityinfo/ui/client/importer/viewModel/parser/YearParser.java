package org.activityinfo.ui.client.importer.viewModel.parser;

import com.google.gwt.regexp.shared.RegExp;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public class YearParser implements FieldParser {
    private static RegExp REGEX = RegExp.compile("^[0-9]{4}$");


    @Override
    public double scoreContent(SourceColumn column) {
        return column.scoreSample(REGEX);
    }
}
