package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.ReferenceValue;

public class ReferenceImporter implements FieldImporter {

    private String fieldName;
    private ResourceId referencedFormId;
    private String[] recordIds;

    public ReferenceImporter(String fieldName, ResourceId referencedFormId, String[] recordIds) {
        this.fieldName = fieldName;
        this.referencedFormId = referencedFormId;
        this.recordIds = recordIds;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public FieldValue getValue(int rowIndex) {
        String recordId = recordIds[rowIndex];
        if(recordId == null) {
            return null;
        }
        return new ReferenceValue(new RecordRef(referencedFormId, ResourceId.valueOf(recordId)));
    }

    public String getRecordId(int rowIndex) {
        return recordIds[rowIndex];
    }
}
