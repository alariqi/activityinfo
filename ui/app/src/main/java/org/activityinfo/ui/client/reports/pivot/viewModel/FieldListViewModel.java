package org.activityinfo.ui.client.reports.pivot.viewModel;

import org.activityinfo.analysis.pivot.viewModel.FormForest;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;

import java.util.ArrayList;
import java.util.List;

/**
 * List of available fields, based on the form selection
 */
public class FieldListViewModel {
    private final FormForest formForest;
    private List<FieldCard> fields = new ArrayList<>();

    public FieldListViewModel(FormForest formForest) {
        this.formForest = formForest;
        for (FormClass formClass : formForest.getAllForms()) {
            for (FormField formField : formClass.getFields()) {
                fields.add(new FieldCard(formClass, formField));
            }
        }
    }

    public List<FieldCard> getFields() {
        return fields;
    }
}