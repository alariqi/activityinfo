package chdc.frontend.client.table;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import org.activityinfo.json.Json;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.RecordTransactionBuilder;
import org.activityinfo.model.resource.RecordUpdate;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.ReferenceValue;
import org.activityinfo.model.type.primitive.TextValue;
import org.activityinfo.model.type.time.LocalDate;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Subscription;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;
import org.activityinfo.ui.client.table.view.LiveRecordGridView;

import java.util.*;
import java.util.logging.Logger;

/**
 * Editable grid widget
 */
public class IncidentGrid implements IsWidget {

    private static final Logger LOGGER = Logger.getLogger(IncidentGrid.class.getName());
    public static final ResourceId FORM_ID = ResourceId.valueOf("incident");

    private final FormStore formStore;
    private final ColumnSetProxy proxy;

    private final Grid<Integer> grid;
    private final ListStore<Integer> gridStore;
    private final PagingLoader loader;

    private Subscription subscription;
    private final Observable<ColumnSet> columnSet;
    private final List<IncidentColumn> columns;

    /**
     * Map from (negative) row index to the new record id.
     *
     */
    private Map<Integer, ResourceId> newRecordMap = new HashMap<>();

    public IncidentGrid(FormStore formStore, TableBanner banner) {

        this.formStore = formStore;

        proxy = new ColumnSetProxy();
        loader = new PagingLoader<>(proxy);
        loader.setRemoteSort(true);

        gridStore = new ListStore<>(index -> index.toString());

        // Create the columns for the table, as well as their editors, etc.
        columns = new ArrayList<>();
        columns.add(new TimeColumn(proxy));
        columns.add(new LocationColumn(formStore, proxy));
        columns.add(new PerpColumn(formStore, proxy));
        columns.add(new TargetColumn(formStore, proxy));
        columns.add(new ActModeColumn(proxy));
        columns.add(new ActColumn(formStore, proxy));
        columns.add(new MeansColumn(formStore, proxy));
        columns.add(new NarrativeColumn(proxy));
        columns.add(new ImpactColumn(proxy));
        columns.add(new NgoSpecColumn(proxy));
        columns.add(new ConfidentialColumn(proxy));

        // Construct the Sencha column model
        List<ColumnConfig<Integer, ?>> configs = new ArrayList<>();
        for (IncidentColumn column : columns) {
            configs.add(column.getColumnConfig());
        }

        // Disable sorting until it's implemented
        for (ColumnConfig<Integer, ?> config : configs) {
            config.setSortable(false);
            config.setMenuDisabled(true);
        }

        // Define the query for columns
        QueryModel queryModel = new QueryModel(FORM_ID);
        queryModel.selectResourceId().as("id");
        for (IncidentColumn column : columns) {
            queryModel.addColumns(column.getColumnsToQuery());
        }

        columnSet = formStore.query(queryModel);
        columnSet.subscribe(this::updateColumnView);

        LiveRecordGridView gridView = new LiveRecordGridView();
        gridView.setColumnLines(true);
        gridView.setTrackMouseOver(false);
        gridView.setStripeRows(true);

        grid = new Grid<>(gridStore, new ColumnModel<>(configs));
        grid.setLoader(loader);
        grid.setLoadMask(true);
        grid.setView(gridView);
        grid.setSelectionModel(new CellSelectionModel<>());

        // Define column editors
        GridInlineEditing<Integer> editing = new GridInlineEditing<>(grid);
        for (IncidentColumn column : columns) {
            if(column.getEditor().isPresent()) {
                editing.addEditor((ColumnConfig)column.getColumnConfig(), column.getEditor().get());
            }
        }

        // Handle toolbar events
        banner.getSaveButton().addSelectHandler(this::onSave);
        banner.getAddButton().addSelectHandler(this::onAdd);
    }



    @Override
    public Widget asWidget() {
        return grid;
    }


    /**
     * Updates the contents of the grid when our store changes or completes loading.
     */
    private void updateColumnView(Observable<ColumnSet> columnSet) {
        if(columnSet.isLoaded()) {
            if(!proxy.push(columnSet.get())) {
                loader.load();
            }
        }
    }

    /**
     * Handles the user's selection of the add button
     */
    private void onAdd(SelectEvent selectEvent) {

        // Our records are keyed by an integer row index so that we
        // can efficiently lookup values in the column-major columnset matrix.

        // To add new records, we'll give them negative indexes to distinguish
        // between records in our ColumnSet matrix and unsaved new records.

        int newIndex = -(newRecordMap.size() + 1);

        newRecordMap.put(newIndex, ResourceId.generateId());

        // Insert the new row at the top of the grid
        gridStore.add(0, newIndex);

    }


    /**
     * Handles the user's selection of the add button
     */
    private void onSave(SelectEvent event) {

        // If the data is not yet loaded, we can't save yet.
        if(columnSet.isLoading()) {
            return;
        }

        // Retrieve the column that contains the row ids.
        ColumnView recordIds = columnSet.get().getColumnView("id");

        // Create a batch of updates to send.
        RecordTransactionBuilder tx = new RecordTransactionBuilder();

        for (Store<Integer>.Record record : gridStore.getModifiedRecords()) {
            int rowIndex = record.getModel();
            String recordId;
            if(rowIndex < 0) {
                // New record
                recordId = newRecordMap.get(rowIndex).asString();
            } else {
                // Existing record
                recordId = recordIds.getString(rowIndex);
            }
            RecordUpdate update = tx.update(FORM_ID, ResourceId.valueOf(recordId));

            for (Store.Change<Integer, ?> change : record.getChanges()) {
                String fieldId = (String) change.getChangeTag();
                if (change.getValue() == null) {
                    update.setFieldValue(fieldId, Json.createNull());
                } else {
                    update.setFieldValue(fieldId, toFieldValue(change.getValue()).toJson());
                }
            }
        }

        formStore.updateRecords(tx.build()).then(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Saving failed :-(");
            }

            @Override
            public void onSuccess(Void result) {
                gridStore.commitChanges();
            }
        });
    }

    private FieldValue toFieldValue(Object value) {
        if(value == null) {
            return null;
        }
        if(value instanceof String) {
            return TextValue.valueOf((String) value);
        }
        if(value instanceof LabeledRecordRef) {
            return new ReferenceValue(((LabeledRecordRef) value).getRecordRef());
        }
        if(value instanceof Date) {
            return new LocalDate(((Date) value));
        }
        throw new UnsupportedOperationException("Unsupported type: " + value);
    }

}
