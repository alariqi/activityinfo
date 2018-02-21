package chdc.frontend.client.table;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class ActModeColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;
    private final ListStore<String> store;
    private final ComboBox<String> field;

    public ActModeColumn(ColumnSetProxy proxy) {

        // Act Mode (Attempted, Perpetrated)
        config = new ColumnConfig<>(proxy.getStringProvider("act_mode"));
        config.setHeader("Act Mode");
        config.setWidth(200);

        store = new ListStore<>(model -> model);
        store.add("Attempted");
        store.add("Perpetrated");

        field = new ComboBox<>(store, model -> model);
        field.setForceSelection(true);
        field.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends Field<?>> getEditor() {
        return Optional.of(field);
    }

}
