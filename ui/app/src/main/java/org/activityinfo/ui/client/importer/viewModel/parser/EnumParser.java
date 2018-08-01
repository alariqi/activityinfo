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
            labels.put(normalize(enumItem.getLabel()), new EnumValue(enumItem));
        }
    }

    static String normalize(String label) {
        return label.trim().toLowerCase();
    }

    @Override
    public double scoreContent(SourceColumn column) {

        String[] sample = column.getSample();
        if(sample.length == 0) {
            return 0;
        }

        int matchingCount = 0;
        for (String input : sample) {
            if (labels.containsKey(normalize(input))) {
                matchingCount++;
            }
        }

        return ((double)matchingCount) / ((double)sample.length);
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return labels.containsKey(normalize(value));
    }

    @Override
    public FieldValue parse(@Nonnull String value) {
        return labels.get(normalize(value));
    }
}
