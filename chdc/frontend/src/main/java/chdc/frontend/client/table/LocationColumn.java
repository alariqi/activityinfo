package chdc.frontend.client.table;

import chdc.frontend.client.cheatsheet.CheatsheetField;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class LocationColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;
    private final CheatsheetField field;

    public LocationColumn(FormSource formSource, ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getStringProvider("location.name"));
        config.setWidth(200);
        config.setHeader("Location");

        LookupKeySet lookupKeySet = LookupKeySet.builder()
                .add("afg_province", "name")
                .add("afg_district", "province", "name")
                .add("afg_settlement", "district", "name")
                .build();

        field = new CheatsheetField(formSource, "Location", lookupKeySet);

    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends Field<?>> getEditor() {
        return Optional.of(field.getField());
    }
}
