package chdc.frontend.client.cheatsheet;

import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.ReferenceValue;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.view.field.FieldUpdater;
import org.activityinfo.ui.client.input.view.field.FieldWidget;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

/**
 * A field widget for reference fields that shows only the leaf key in the
 * main form, but provides a popup with the complete selection of keys.
 */
public class CheatsheetFieldWidget implements FieldWidget {

    private final FormField field;
    private final LookupViewModel viewModel;
    private final CheatsheetCombo combo;
    private final FieldUpdater fieldUpdater;

    private final SlideoutPanel slideOutPanel;

    public CheatsheetFieldWidget(FormSource formSource, FormTree formTree, FormField field, FieldUpdater fieldUpdater) {
        this.field = field;
        this.fieldUpdater = fieldUpdater;
        this.viewModel = new LookupViewModel(formSource, new LookupKeySet(formTree, field));


        combo = new CheatsheetCombo(viewModel, this.viewModel.getLeafLookupKey());

        // Setup the slideout panel with the cheatsheet

        CheatsheetPanel cheatsheet = new CheatsheetPanel(viewModel);

        slideOutPanel = new SlideoutPanel();
        slideOutPanel.setTitleHeading(I18N.MESSAGES.findFieldValue(field.getLabel()));
        slideOutPanel.add(cheatsheet);


        // Add the slide out panel to the dom when this field is attached,
        // but clean up after ourselves after we are removed.
        // This ensures that the overlay does not stick around in the event
        // of browser navigation, among other things!
        combo.addAttachHandler(event -> {
            if(event.isAttached()) {
                slideOutPanel.attach();
            } else {
                slideOutPanel.detach();
            }
        });

        combo.addTriggerHandler(event -> slideOutPanel.show());

    }

    @Override
    public void init(FieldValue value) {
        ReferenceValue referenceValue = (ReferenceValue) value;
        viewModel.setInitialSelection(referenceValue.getReferences());
    }

    @Override
    public void clear() {
        viewModel.clearSelection();
    }

    @Override
    public void setRelevant(boolean relevant) {
    }

    @Override
    public Widget asWidget() {
        return combo.asWidget();
    }
}
