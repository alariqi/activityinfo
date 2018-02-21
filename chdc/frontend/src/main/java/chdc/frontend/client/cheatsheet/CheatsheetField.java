package chdc.frontend.client.cheatsheet;

import com.sencha.gxt.widget.core.client.form.Field;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

/**
 * A field widget for reference fields that shows only the leaf key in the
 * main form, but provides a popup with the complete selection of keys.
 */
public class CheatsheetField {

    private final LookupViewModel viewModel;
    private final CheatsheetCombo combo;

    private final SlideoutPanel slideOutPanel;

    public CheatsheetField(FormSource formSource, String fieldLabel, LookupKeySet lookupKeySet) {
        this.viewModel = new LookupViewModel(formSource, lookupKeySet);

        combo = new CheatsheetCombo(viewModel, this.viewModel.getLeafLookupKey());

        // Setup the slideout panel with the cheatsheet

        CheatsheetPanel cheatsheet = new CheatsheetPanel(viewModel);

        slideOutPanel = new SlideoutPanel();
        slideOutPanel.setTitleHeading(I18N.MESSAGES.findFieldValue(fieldLabel));
        slideOutPanel.add(cheatsheet);
        slideOutPanel.attach();

        combo.addTriggerHandler(event -> slideOutPanel.show());

    }

    public Field<String> getField() {
        return combo.getComboBox();
    }
}
