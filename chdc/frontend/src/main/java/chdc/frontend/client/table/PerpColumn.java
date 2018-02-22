package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;
import org.activityinfo.ui.client.table.view.LabeledReference;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PerpColumn implements IncidentColumn {

    private final ColumnConfig<Integer, LabeledReference> config;

    public PerpColumn(FormSource formSource, ColumnSetProxy proxy) {

        config = new ColumnConfig<>(proxy.getLabeledRefProvider("actor", "actor.name"));
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
    public List<ColumnModel> getColumnsToQuery() {
        return Arrays.asList(new ColumnModel("actor"), new ColumnModel("actor.name"));
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends IsField<?>> getEditor() {
//        return Optional.of(cheatsheet.getField());
        return Optional.empty();
    }
}
