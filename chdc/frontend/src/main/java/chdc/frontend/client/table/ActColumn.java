package chdc.frontend.client.table;

import chdc.frontend.client.cheatsheet.CheatsheetField;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class ActColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;
    private final CheatsheetField cheatsheet;

    public ActColumn(FormSource formSource, ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getStringProvider("act.name"));
        config.setWidth(200);
        config.setHeader("Act");

        LookupKeySet lookupKeySet = LookupKeySet.builder()
                .add("act", "category")
                .add("act", "sub_category")
                .add("act", "range")
                .add("act", "name")
                .build();

        cheatsheet = new CheatsheetField(formSource, "Act", lookupKeySet);
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
