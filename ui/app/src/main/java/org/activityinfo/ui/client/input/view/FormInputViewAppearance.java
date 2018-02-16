package org.activityinfo.ui.client.input.view;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.view.field.FieldWidgetFactory;

/**
 * Defines the appearance of the form input view
 */
public interface FormInputViewAppearance {

    FieldWidgetFactory createFactory(FormSource formSource, FormTree tree);

}
