package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class NarrativeColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;
    private final TextField editor;

    public NarrativeColumn(ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getStringProvider("narrative"));
        config.setHeader("Narrative");
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
