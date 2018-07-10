package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.type.FieldType;
import org.activityinfo.ui.client.analysis.viewModel.FieldTypes;

public class FieldListItem {
    String type;
    String formLabel;
    String fieldLabel;
    String formula;
    boolean included;

    FieldListItem() {

    }

    public FieldListItem(FormTree.Node node) {
        this.type = FieldTypes.localizedFieldType(node.getType());
        this.formLabel = node.getDefiningFormClass().getLabel();
        this.fieldLabel = node.getField().getLabel();
    }

    public FieldListItem(String formLabel, FieldType fieldType, String fieldLabel) {
        this.type = FieldTypes.localizedFieldType(fieldType);
        this.formLabel = formLabel;
        this.fieldLabel = fieldLabel;
    }

    public FieldListItem(String formLabel, FormField field) {
        this.type = FieldTypes.localizedFieldType(field.getType());
        this.formLabel = formLabel;
        this.fieldLabel = field.getLabel();
    }

    public FieldListItem(String type, String formLabel, String fieldLabel) {
        this.type = type;
        this.formLabel = formLabel;
        this.fieldLabel = fieldLabel;
    }

    public boolean isIncluded() {
        return included;
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

    public static FieldListItemBuilder builder() {
        return new FieldListItemBuilder();
    }
}
