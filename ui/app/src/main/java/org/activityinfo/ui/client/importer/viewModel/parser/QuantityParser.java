package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.number.Quantity;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;

public class QuantityParser implements FieldParser {
    @Override
    public double scoreContent(SourceColumn column) {
        return column.getNumberFraction();
    }

    @Override
    public boolean validate(@Nonnull String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FieldValue parse(@Nonnull String value) {
        return new Quantity(Double.parseDouble(value));
    }
}
