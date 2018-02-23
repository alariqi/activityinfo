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

/**
 * Base class for perperator and intended target, which both
 * reference the actor table.
 */
public abstract class ActorColumn implements IncidentColumn {

    private static final ResourceId ACTOR_FORM_ID = ResourceId.valueOf("afg_actor");

    private final String idColumn;
    private final String nameColumn;

    private final ColumnConfig<Integer, LabeledRecordRef> config;

    private final CheatsheetComboBox cheatsheet;

    public ActorColumn(FormSource formSource, ColumnSetProxy proxy, String columnHeader, String fieldName) {

        idColumn = fieldName;
        nameColumn = fieldName + ".name";

        config = new ColumnConfig<>(proxy.getLabeledRefProvider(ACTOR_FORM_ID, idColumn, nameColumn));
        config.setHeader(columnHeader);
        config.setWidth(200);

        LookupKeySet lookupKeySet = LookupKeySet.builder()
                .add("actor_category", "state")
                .add("actor_category", "state_level")
                .add("actor_category", "civilian")
                .add("actor_category", "armed")
                .add("actor", "category", "name")
                .build();

        cheatsheet = new CheatsheetComboBox(formSource, columnHeader, lookupKeySet);
    }

    @Override
    public List<ColumnModel> getColumnsToQuery() {
        return Arrays.asList(new ColumnModel(idColumn), new ColumnModel(nameColumn));
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
