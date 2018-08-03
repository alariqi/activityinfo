package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.barcode.BarcodeValue;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class BarcodeParser implements FieldParser {
    @Override
    public double scoreContent(SourceColumn column) {
        return ANYTHING_GOES_SCORE;
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return true;
    }

    @Override
    public FieldValue parse(@Nonnull String value) {
        return BarcodeValue.valueOf(value);
    }
}