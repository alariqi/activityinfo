package org.activityinfo.ui.client.table.view;

public class LabeledReference {
    private String recordId;
    private String label;

    public LabeledReference(String recordId, String label) {
        this.recordId = recordId;
        this.label = label;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
