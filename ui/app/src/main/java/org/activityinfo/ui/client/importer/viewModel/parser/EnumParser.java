package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.enumerated.EnumValue;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class EnumParser implements FieldParser {
    private Map<String, EnumValue> labels = new HashMap<>();

    public EnumParser(EnumType enumType) {
        for (EnumItem enumItem : enumType.getValues()) {
            labels.put(enumItem.getLabel(), new EnumValue(enumItem));
        }
    }

    @Override
    public double scoreContent(SourceColumn column) {
        return column.scoreSample(labels.keySet());
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return labels.containsKey(value);
    }

    @Override
    public FieldValue parse(@Nonnull String value) {
        return labels.get(value);
    }
}
