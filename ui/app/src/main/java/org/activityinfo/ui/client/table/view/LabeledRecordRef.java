package org.activityinfo.ui.client.table.view;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;

public class LabeledRecordRef {
    private RecordRef recordRef;
    private String label;

    public LabeledRecordRef(ResourceId formId, String recordId, String label) {
        this.recordRef = new RecordRef(formId, ResourceId.valueOf(recordId));
        this.label = label;
    }

    public LabeledRecordRef(RecordRef ref, String label) {
        this.recordRef = ref;
        this.label = label;
    }

    public String getRecordId() {
        return recordRef.getRecordId().asString();
    }

    public RecordRef getRecordRef() {
        return recordRef;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

}
