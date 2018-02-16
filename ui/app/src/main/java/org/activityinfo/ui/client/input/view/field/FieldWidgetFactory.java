package org.activityinfo.ui.client.input.view.field;

import org.activityinfo.model.form.FormField;

/**
 * Created by alex on 16-2-18.
 */
public interface FieldWidgetFactory {
    FieldWidget create(FormField field, FieldUpdater updater);
}
