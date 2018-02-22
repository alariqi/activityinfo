package chdc.frontend.client.table;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Subscription;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;
import org.activityinfo.ui.client.table.view.LiveRecordGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Editable grid widget
 */
public class IncidentGrid implements IsWidget {

    private static final Logger LOGGER = Logger.getLogger(IncidentGrid.class.getName());

    private final ColumnSetProxy proxy;
    private final PagingLoader loader;
    private final ListStore<Integer> store;
    private final Grid<Integer> grid;

    private Subscription subscription;
    private final Observable<ColumnSet> columnSet;

    public IncidentGrid(FormSource formSource) {

        proxy = new ColumnSetProxy();
        loader = new PagingLoader<>(proxy);
        loader.setRemoteSort(true);

        store = new ListStore<>(index -> index.toString());

        // Create the columns for the table, as well as their editors, etc.
        List<IncidentColumn> columns = new ArrayList<>();
        columns.add(new TimeColumn(proxy));
        columns.add(new LocationColumn(formSource, proxy));
        columns.add(new PerpColumn(formSource, proxy));
        columns.add(new TargetColumn(proxy));
        columns.add(new ActModeColumn(proxy));
        columns.add(new ActColumn(formSource, proxy));
        columns.add(new MeansColumn(formSource, proxy));
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
        QueryModel queryModel = new QueryModel(ResourceId.valueOf("incident"));
        for (IncidentColumn column : columns) {
            queryModel.addColumns(column.getColumnsToQuery());
        }

        columnSet = formSource.query(queryModel);
        columnSet.subscribe(this::updateColumnView);

        LiveRecordGridView gridView = new LiveRecordGridView();
        gridView.setColumnLines(true);
        gridView.setTrackMouseOver(false);
        gridView.setStripeRows(true);

        grid = new Grid<>(store, new ColumnModel<>(configs));
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

    @Override
    public Widget asWidget() {
        return grid;
    }
}
