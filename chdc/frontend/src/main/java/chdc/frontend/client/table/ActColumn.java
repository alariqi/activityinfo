package chdc.frontend.client.table;

import chdc.frontend.client.cheatsheet.CheatsheetComboBox;
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

public class ActColumn implements IncidentColumn {

    private static final ResourceId REF_FORM_ID = ResourceId.valueOf("act");

    private final ColumnConfig<Integer, LabeledRecordRef> config;
    private final CheatsheetComboBox cheatsheet;

    public ActColumn(FormSource formSource, ColumnSetProxy proxy) {

        config = new ColumnConfig<>(proxy.getLabeledRefProvider(REF_FORM_ID, "act", "act.name"));
        config.setWidth(200);
        config.setHeader("Act");

        LookupKeySet lookupKeySet = LookupKeySet.builder()
                .add("act", "category")
                .add("act", "sub_category")
                .add("act", "range")
                .add("act", "name")
                .build();

        cheatsheet = new CheatsheetComboBox(formSource, "Act", lookupKeySet);
    }

    @Override
    public List<ColumnModel> getColumnsToQuery() {
        return Arrays.asList(
                new ColumnModel("act"),
                new ColumnModel("act.name"));
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
