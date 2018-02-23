package chdc.frontend.client.cheatsheet;

import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;


public class CheatsheetComboBox extends ComboBox<LabeledRecordRef> {
    public CheatsheetComboBox(FormSource formSource, String fieldLabel, LookupKeySet lookupKeySet) {
        super(new CheatsheetComboCell(new LookupViewModel(formSource, lookupKeySet), fieldLabel));
    }
}
