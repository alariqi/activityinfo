package chdc.frontend.client.table;

import chdc.frontend.client.cheatsheet.CheatsheetField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MeansColumn implements IncidentColumn {

    private static final ResourceId REF_FORM_ID = ResourceId.valueOf("means");

    private final ColumnConfig<Integer, LabeledRecordRef> config;
    private final CheatsheetField cheatsheet;

    public MeansColumn(FormSource formSource, ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getLabeledRefProvider(REF_FORM_ID, "means", "means.name"));
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
    public List<ColumnModel> getColumnsToQuery() {
        return Arrays.asList(new ColumnModel("means"), new ColumnModel("means.name"));
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends IsField<?>> getEditor() {
        return Optional.of(cheatsheet);
    }
}
