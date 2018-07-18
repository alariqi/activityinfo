package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class EnumParser implements FieldParser {
    private Set<String> labels = new HashSet<>();

    public EnumParser(EnumType enumType) {
        for (EnumItem enumItem : enumType.getValues()) {
            labels.add(enumItem.getLabel());
        }
    }

    @Override
    public double scoreContent(SourceColumn column) {
        return column.scoreSample(labels);
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return labels.contains(value);
    }
}
