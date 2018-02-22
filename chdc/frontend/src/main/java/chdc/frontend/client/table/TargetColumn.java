package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;
import org.activityinfo.ui.client.table.view.LabeledReference;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TargetColumn implements IncidentColumn {

    private final ColumnConfig<Integer, LabeledReference> config;

    public TargetColumn(ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getLabeledRefProvider("primary_target", "primary_target.name"));
        config.setHeader("Primary Target");
        config.setWidth(200);
    }

    @Override
    public List<ColumnModel> getColumnsToQuery() {
        return Arrays.asList(new ColumnModel("primary_target"), new ColumnModel("primary_target.name"));
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
