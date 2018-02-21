package chdc.frontend.client.table;

import chdc.frontend.client.cheatsheet.CheatsheetField;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class MeansColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;
    private final CheatsheetField cheatsheet;

    public MeansColumn(FormSource formSource, ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getStringProvider("means.name"));
        config.setWidth(200);
        config.setHeader("Means");

        LookupKeySet lookupKeySet = LookupKeySet.builder()
                .add("means", "type")
                .add("means", "scale")
                .add("means", "name")
                .build();

        cheatsheet = new CheatsheetField(formSource, "Means", lookupKeySet);
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends Field<?>> getEditor() {
        return Optional.of(cheatsheet.getField());
    }
}
