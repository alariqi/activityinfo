package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.analysis.viewModel.FieldTypes;

public class FieldListItemBuilder {
    private FieldListItem item = new FieldListItem();

    public FieldListItemBuilder included(boolean included) {
        item.included = included;
        return this;
    }

    public FieldListItemBuilder form(String label) {
        item.formLabel = label;
        return this;
    }

    public FieldListItemBuilder rootField(FormField field) {
        item.formLabel = I18N.CONSTANTS.thisForm();
        return field(field);
    }

    public FieldListItemBuilder field(FormField field) {
        item.type = FieldTypes.localizedFieldType(field.getType());
        item.fieldLabel = field.getLabel();
        return this;
    }

    public FieldListItemBuilder formula(String formulaString) {
        item.formula = formulaString;
        return this;
    }

    public FieldListItem build() {
        return item;
    }

}
