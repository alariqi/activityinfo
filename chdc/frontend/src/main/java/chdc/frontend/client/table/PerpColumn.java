package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Optional;

public class PerpColumn implements IncidentColumn {

    private final ColumnConfig<Integer, String> config;

    public PerpColumn(FormSource formSource, ColumnSetProxy proxy) {

        config = new ColumnConfig<>(proxy.getStringProvider("actor.name"));
        config.setHeader("Perpetrator");
        config.setWidth(200);
//
//        LookupKeySet lookupKeySet = LookupKeySet.builder()
//                .add("actor_category", "state")
//                .add("actor_category", "state_level")
//                .add("actor_category", "civilian")
//                .add("actor_category", "armed")
//                .add("actor", "category", "name")
//                .build();
//
//        cheatsheet = new CheatsheetField(formSource, "Actpr", lookupKeySet);
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends Field<?>> getEditor() {
//        return Optional.of(cheatsheet.getField());
        return Optional.empty();
    }
}
