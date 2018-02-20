package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class ConfidentialColumn implements IncidentColumn {

    private final ColumnConfig config;
    private final TextField editor;

    public ConfidentialColumn(ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getStringProvider("confidential_notes"));
        config.setHeader("Confidential Note");
        config.setWidth(200);

        editor = new TextField();
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends Field<?>> getEditor() {
        return Optional.of(editor);
    }
}
