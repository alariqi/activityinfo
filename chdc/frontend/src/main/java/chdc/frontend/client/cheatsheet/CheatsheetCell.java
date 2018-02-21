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

/**
 * Variant of a ComboBox cell that has a trigger which opens a cheatsheet popup.
 *
 */
public class CheatsheetCell extends ComboBoxCell<String> {


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

    public CheatsheetCell(ListStore<String> store) {
        super(store, key -> key, new HelperTriggerAppearance());
        setForceSelection(true);
        setUseQueryCache(false);
        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
    }

    @Override
    public void finishEditing(Element parent, String value, Object key, ValueUpdater<String> valueUpdater) {
        // NOOP
    }

}
