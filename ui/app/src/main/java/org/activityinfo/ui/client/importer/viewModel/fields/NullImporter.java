package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.type.FieldValue;

public class NullImporter implements FieldImporter {

    private final FormField field;

    public NullImporter(FormField field) {
        this.field = field;
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public FieldValue getValue(int rowIndex) {
        return null;
    }
}
