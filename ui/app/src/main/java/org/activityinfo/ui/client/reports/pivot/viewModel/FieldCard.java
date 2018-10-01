package org.activityinfo.ui.client.reports.pivot.viewModel;

import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.analysis.viewModel.FieldTypes;
import org.activityinfo.ui.client.base.cardlist.Card;

public class FieldCard implements Card {
    private final FormClass formClass;
    private final FormField formField;

    public FieldCard(FormClass formClass, FormField formField) {
        this.formClass = formClass;
        this.formField = formField;
    }

    @Override
    public String getId() {
        return formClass.getId() + "." + formField.getId();
    }

    @Override
    public String getSurtitle() {
        return FieldTypes.localizedFieldType(formField.getType()) + " — " + formClass.getLabel();
    }

    @Override
    public String getLabel() {
        return formField.getLabel();
    }
}
