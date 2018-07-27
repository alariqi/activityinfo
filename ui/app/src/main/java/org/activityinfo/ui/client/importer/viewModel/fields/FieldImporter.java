package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.type.FieldValue;

public interface FieldImporter {

    String getFieldName();

    FieldValue getValue(int rowIndex);
}
