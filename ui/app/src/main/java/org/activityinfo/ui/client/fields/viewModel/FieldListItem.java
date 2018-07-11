package org.activityinfo.ui.client.fields.viewModel;

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

    public FieldListItem(String formLabel, FieldType fieldType, String fieldLabel) {
        this.type = FieldTypes.localizedFieldType(fieldType);
        this.formLabel = formLabel;
        this.fieldLabel = fieldLabel;
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

    public String getFormula() {
        return formula;
    }

    public static FieldListItemBuilder builder() {
        return new FieldListItemBuilder();
    }
}
