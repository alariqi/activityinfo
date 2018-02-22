package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NgoSpecColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;

    public NgoSpecColumn(ColumnSetProxy proxy) {
        config = new ColumnConfig<>(new TodoValueProvider("ngo"));
        config.setHeader("NGO Specification");
        config.setWidth(200);
    }

    @Override
    public List<ColumnModel> getColumnsToQuery() {
        return Collections.emptyList();
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends IsField<?>> getEditor() {
        return Optional.empty();
    }
}
