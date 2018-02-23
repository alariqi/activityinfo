package chdc.frontend.client.cheatsheet;

import chdc.theme.client.base.field.Css3TriggerFieldAppearance;
import chdc.theme.client.base.field.Css3TriggerFieldAppearance.Css3TriggerFieldResources;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.ListStore;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;

/**
 * Variant of a ComboBox cell that has a trigger which opens a cheatsheet popup.
 *
 */
public class CheatsheetComboCell extends ComboBoxCell<LabeledRecordRef> {


    public interface HelperTriggerResources extends Css3TriggerFieldResources {

        @Override
        @ClientBundle.Source({
                "chdc/theme/client/base/field/Css3ValueBaseField.gss",
                "chdc/theme/client/base/field/Css3TextField.gss",
                "help.gss"})
        HelperTriggerStyles style();

        @ClientBundle.Source("help.png")
        ImageResource helpTrigger();
    }

    public interface HelperTriggerStyles extends Css3TriggerFieldAppearance.Css3TriggerFieldStyle { }

    private static class HelperTriggerAppearance extends Css3TriggerFieldAppearance {

        public HelperTriggerAppearance() {
            super(GWT.create(HelperTriggerResources.class));
        }
    }

    public CheatsheetComboCell(ListStore<LabeledRecordRef> store) {
        super(store, key -> key.getLabel(), new HelperTriggerAppearance());
        setForceSelection(true);
        setUseQueryCache(false);
        setTriggerAction(TriggerAction.QUERY);
    }

//    @Override
//    public void doQuery(Context context, XElement parent, ValueUpdater<LabeledReference> updater, LabeledReference value, String query, boolean force) {
//        // DO NOTHING
//        // We do not want to display the drop down list.
//    }

    @Override
    public void finishEditing(Element parent, LabeledRecordRef value, Object key, ValueUpdater<LabeledRecordRef> valueUpdater) {
        super.finishEditing(parent, value, key, valueUpdater);
    }
}
