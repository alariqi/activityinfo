package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.model.annotation.AutoBuilder;

@AutoBuilder
public class ReportElement {
    String id;
    String label;
    String type;
    String formLabel;
    
    ReportElement() {}

    public ReportElement(String id, String label, String type, String formLabel) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.formLabel = formLabel;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public String getFormLabel() {
        return formLabel;
    }
}
