package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

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
    public List<ColumnModel> getColumnsToQuery() {
        return singletonList(new ColumnModel("confidential_notes"));
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends IsField<?>> getEditor() {
        return Optional.of(editor);
    }

}
