package chdc.frontend.client.entry;

import chdc.frontend.client.cheatsheet.CheatsheetField;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.view.field.FieldUpdater;
import org.activityinfo.ui.client.input.view.field.FieldWidget;
import org.activityinfo.ui.client.input.view.field.FieldWidgetFactory;
import org.activityinfo.ui.client.input.view.field.FieldWidgetFactoryImpl;

public class IncidentFieldFactory implements FieldWidgetFactory {

    private final FormSource formSource;
    private final FormTree formTree;
    private FieldWidgetFactoryImpl delegate;

    public IncidentFieldFactory(FormSource formSource, FormTree formTree) {
        this.formSource = formSource;
        this.formTree = formTree;
        delegate = new FieldWidgetFactoryImpl(formSource, formTree);
    }

    @Override
    public FieldWidget create(FormField field, FieldUpdater updater) {
        if(field.getId().asString().equals("act")) {
            return createActWidget(field, updater);
        }
        return delegate.create(field, updater);
    }

    private FieldWidget createActWidget(FormField field, FieldUpdater updater) {
        return new CheatsheetField(formSource, formTree, field, updater);
    }
}
