package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class NgoSpecColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;

    public NgoSpecColumn(ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getStringProvider("'#TODO'"));
        config.setHeader("NGO Specification");
        config.setWidth(200);
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends Field<?>> getEditor() {
        return Optional.empty();
    }
}
