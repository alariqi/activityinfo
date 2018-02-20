package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class MeansColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;

    public MeansColumn(ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getStringProvider("means.name"));
        config.setWidth(200);
        config.setHeader("Means");

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
