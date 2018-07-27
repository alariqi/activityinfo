package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;

public class SimpleFieldImporter implements FieldImporter {

    private String fieldName;
    private final ColumnView columnView;
    private final FieldParser parser;

    public SimpleFieldImporter(FormField field, ColumnView columnView, FieldParser parser) {
        this.fieldName = field.getName();
        this.columnView = columnView;
        this.parser = parser;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public FieldValue getValue(int rowIndex) {
        String value = columnView.getString(rowIndex);
        if(value == null) {
            return null;
        }
        return parser.parse(value);
    }
}
