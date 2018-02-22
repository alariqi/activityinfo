package chdc.frontend.client.table;

import chdc.frontend.client.cheatsheet.CheatsheetField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.observable.Observable;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;
import org.activityinfo.ui.client.table.view.LabeledReference;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LocationColumn implements IncidentColumn {

    private final ColumnConfig<Integer, LabeledReference> config;
    private final CheatsheetField field;

    public LocationColumn(FormSource formSource, ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getLabeledRefProvider("location", "location.name"));
        config.setWidth(200);
        config.setHeader("Location");

        // This is the remote column
        Observable<ColumnView> remoteColumn;

        LookupKeySet lookupKeySet = LookupKeySet.builder()
                .add("afg_province", "name")
                .add("afg_district", "province", "name")
                .add("afg_settlement", "district", "name")
                .build();

        field = new CheatsheetField(formSource, "Location", lookupKeySet);
    }

    @Override
    public List<ColumnModel> getColumnsToQuery() {
        return Arrays.asList(new ColumnModel("location"), new ColumnModel("location.name"));
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends IsField<?>> getEditor() {
        return Optional.of(field);
    }
}
