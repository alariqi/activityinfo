package chdc.frontend.client.table;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Collections.singletonList;

public class NarrativeColumn implements IncidentColumn {

    private final Logger LOGGER = Logger.getLogger(NarrativeColumn.class.getName());

    private final ColumnConfig<Integer, String> config;
    private final TextField editor;

    public NarrativeColumn(ColumnSetProxy proxy) {
        ValueProvider<Integer, String> delegate = proxy.getStringProvider("narrative");
        ValueProvider<Integer, String> column = new ValueProvider<Integer, String>() {
            @Override
            public String getValue(Integer object) {
                return delegate.getValue(object);
            }

            @Override
            public void setValue(Integer object, String value) {
                LOGGER.info("Narrative[" + object + "] = " + value);
            }

            @Override
            public String getPath() {
                return delegate.getPath();
            }
        };

        config = new ColumnConfig<>(column);
        config.setHeader("Narrative");
        config.setWidth(200);

        editor = new TextField();
    }

    @Override
    public List<ColumnModel> getColumnsToQuery() {
        return singletonList(new ColumnModel("narrative"));
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
