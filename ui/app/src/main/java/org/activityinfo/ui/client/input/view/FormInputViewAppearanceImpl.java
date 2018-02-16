package org.activityinfo.ui.client.input.view;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.view.field.FieldWidgetFactory;
import org.activityinfo.ui.client.input.view.field.FieldWidgetFactoryImpl;

public class FormInputViewAppearanceImpl implements FormInputViewAppearance {
    @Override
    public FieldWidgetFactory createFactory(FormSource formSource, FormTree tree) {
        return new FieldWidgetFactoryImpl(formSource, tree);
    }
}
