package chdc.frontend.client.table;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Subscription;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;
import org.activityinfo.ui.client.table.view.LiveRecordGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Editable grid widget
 */
public class IncidentGrid implements IsWidget {


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

        List<ColumnConfig<Integer, ?>> columns = new ArrayList<>();

        // Time:
        // Time will be one cell which includes both a textfield with a datepicker dropdown and a
        // second smaller textfield where the time can be entered in 24 hour format. The time
        // textfield will round to the nearest hour and validate the datetime automatically.


        ColumnConfig<Integer, Date> timeColumn = new ColumnConfig<>(proxy.getDateProvider("date"));
        timeColumn.setHeader(I18N.CONSTANTS.time());
        timeColumn.setWidth(200);
        timeColumn.setCell(new DateCell(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.YEAR_MONTH_DAY)));
        columns.add(timeColumn);

        // Location
        ColumnConfig<Integer, String> locationColumn = new ColumnConfig<>(proxy.getStringProvider("location.name"));
        locationColumn.setWidth(200);
        locationColumn.setHeader("Location");
        columns.add(locationColumn);

        // Perpetrator
        ColumnConfig<Integer, String> perpetratorColumn = new ColumnConfig<>(proxy.getStringProvider("actor.name"));
        perpetratorColumn.setHeader("Perpetrator");
        perpetratorColumn.setWidth(200);
        columns.add(perpetratorColumn);

        // Primary intended target
        ColumnConfig<Integer, String> targetColumn = new ColumnConfig<>(proxy.getStringProvider("primary_target.name"));
        targetColumn.setHeader("Primary Target");
        targetColumn.setWidth(200);
        columns.add(targetColumn);

        // Act Mode (Attempted, Perpetrated)
        ColumnConfig<Integer, String> actModeColumn = new ColumnConfig<>(proxy.getStringProvider("act_mode"));
        actModeColumn.setHeader("Act Mode");
        actModeColumn.setWidth(200);
        columns.add(actModeColumn);

        // Means
        ColumnConfig<Integer, String> meansColumn = new ColumnConfig<>(proxy.getStringProvider("means.name"));
        meansColumn.setWidth(200);
        meansColumn.setHeader("Means");
        columns.add(meansColumn);

        // Narrative
        ColumnConfig<Integer, String> narrativeColumn = new ColumnConfig<>(proxy.getStringProvider("narrative"));
        narrativeColumn.setHeader("Narrative");
        narrativeColumn.setWidth(200);
        columns.add(narrativeColumn);

        // Impact
        ColumnConfig<Integer, String> impactColumn = new ColumnConfig<>(proxy.getStringProvider("'#TODO'"));
        impactColumn.setHeader("Impact");
        impactColumn.setWidth(200);
        columns.add(impactColumn);

        // NGO Impact
        ColumnConfig<Integer, String> ngoColumn = new ColumnConfig<>(proxy.getStringProvider("'#TODO'"));
        ngoColumn.setHeader("NGO Specification");
        ngoColumn.setWidth(200);
        columns.add(ngoColumn);

        // Disable sorting until it's implemented
        for (ColumnConfig<Integer, ?> column : columns) {
            column.setSortable(false);
            column.setMenuDisabled(true);
        }

        ColumnModel<Integer> columnModel = new ColumnModel<>(columns);

        // Define the query for columns
        QueryModel queryModel = new QueryModel(ResourceId.valueOf("incident"));
        for (ColumnConfig<Integer, ?> column : columns) {
            queryModel.selectExpr(column.getValueProvider().getPath()).as(column.getValueProvider().getPath());
        }

        columnSet = formSource.query(queryModel);
        columnSet.subscribe(this::updateColumnView);

        LiveRecordGridView gridView = new LiveRecordGridView();
        gridView.setColumnLines(true);
        gridView.setTrackMouseOver(false);
        gridView.setStripeRows(true);

        grid = new Grid<>(store, columnModel);
        grid.setLoader(loader);
        grid.setLoadMask(true);
        grid.setView(gridView);
        grid.setSelectionModel(new CellSelectionModel<>());
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
