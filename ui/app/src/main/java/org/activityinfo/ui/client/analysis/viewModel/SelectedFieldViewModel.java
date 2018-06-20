package org.activityinfo.ui.client.analysis.viewModel;

import org.activityinfo.model.formTree.FormTree;

public class SelectedFieldViewModel {
    private String type;
    private String formLabel;
    private String fieldLabel;


    public SelectedFieldViewModel(FormTree.Node node) {
        this.type = FieldTypes.localizedFieldType(node.getType());
        this.formLabel = node.getDefiningFormClass().getLabel();
        this.fieldLabel = node.getField().getLabel();
    }

    public String getType() {
        return type;
    }

    public String getFormLabel() {
        return formLabel;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }
}
