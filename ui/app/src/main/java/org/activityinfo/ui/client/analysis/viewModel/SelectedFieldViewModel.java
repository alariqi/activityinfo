package org.activityinfo.ui.client.analysis.viewModel;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.type.FieldType;

public class SelectedFieldViewModel {
    private String type;
    private String formLabel;
    private String fieldLabel;


    public SelectedFieldViewModel(FormTree.Node node) {
        this.type = FieldTypes.localizedFieldType(node.getType());
        this.formLabel = node.getDefiningFormClass().getLabel();
        this.fieldLabel = node.getField().getLabel();
    }

    public SelectedFieldViewModel(String formLabel, FieldType fieldType, String fieldLabel) {
        this.type = FieldTypes.localizedFieldType(fieldType);
        this.formLabel = formLabel;
        this.fieldLabel = fieldLabel;
    }

    public SelectedFieldViewModel(String formLabel, FormField field) {
        this.type = FieldTypes.localizedFieldType(field.getType());
        this.formLabel = formLabel;
        this.fieldLabel = field.getLabel();
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
