package chdc.frontend.client.cheatsheet;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import org.activityinfo.observable.Observable;

import java.util.List;

/**
 * Variant of a ComboBox cell that has a trigger which opens a cheatsheet popup.
 */
public class CheatsheetCell extends ComboBoxCell<String> {


    interface Style extends CssResource {
        String message();
    }

    private Observable<List<String>> observable;


    public CheatsheetCell(Observable<List<String>> observable, ListStore<String> store) {
        super(store, key -> key);
        this.observable = observable;
        setForceSelection(true);
        setUseQueryCache(false);
        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
    }

    @Override
    public void doQuery(Context context, XElement parent, ValueUpdater<String> updater, String value, String query, boolean force) {
        // DO NOTHING
        // We do not want to display the drop down list.
    }

    @Override
    public void finishEditing(Element parent, String value, Object key, ValueUpdater<String> valueUpdater) {
        // NOOP
    }
}
