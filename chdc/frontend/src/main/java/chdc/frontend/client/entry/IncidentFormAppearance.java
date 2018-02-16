package chdc.frontend.client.entry;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.view.FormInputViewAppearance;
import org.activityinfo.ui.client.input.view.field.FieldWidgetFactory;

public class IncidentFormAppearance implements FormInputViewAppearance {
    @Override
    public FieldWidgetFactory createFactory(FormSource formSource, FormTree tree) {
        return new IncidentFieldFactory(formSource, tree);
    }
}
