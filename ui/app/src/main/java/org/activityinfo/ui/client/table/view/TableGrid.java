/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.table.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import org.activityinfo.analysis.table.EffectiveTableColumn;
import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.base.menu.Css3MenuAppearance;
import org.activityinfo.ui.client.base.menu.MenuArrow;
import org.activityinfo.ui.client.table.model.TableUpdater;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;

import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;


public class TableGrid implements IsWidget {

    private static final Logger LOGGER = Logger.getLogger(TableGrid.class.getName());

    private final TableViewModel viewModel;
    private final TableUpdater tableUpdater;

    private final ListStore<Integer> store;
    private final Grid<Integer> grid;

    private final SubscriptionSet subscriptions = new SubscriptionSet();
    private final ColumnSetProxy proxy;
    private final PagingLoader<PagingLoadConfig, PagingLoadResult<Integer>> loader;

    private EffectiveTableModel currentTableModel = null;
    private ColumnModel<Integer> pendingColumnModel = null;

    public TableGrid(TableViewModel viewModel, TableUpdater tableUpdater) {
        this.viewModel = viewModel;
        this.tableUpdater = tableUpdater;

        // GXT Grid's are built around row-major data storage, while AI uses
        // Column-major order here. So we construct fake loaders/stores that represent
        // each row as row index.

        proxy = new ColumnSetProxy();
        loader = new PagingLoader<>(proxy);
        loader.setRemoteSort(true);

        store = new ListStore<>(index -> index.toString());

        // Build a grid column model based on the user's selection of columns
        ColumnModelBuilder columns = new ColumnModelBuilder(proxy);
        if(viewModel.getEffectiveTable().isLoaded()) {
            EffectiveTableModel table = viewModel.getEffectiveTable().get();
            columns.addAll(table.getColumns());
            currentTableModel = table;

        } else {
            columns.addLoadingColumns();
        }

        LiveRecordGridView gridView = new LiveRecordGridView();
        gridView.setColumnLines(true);
        gridView.setTrackMouseOver(false);
        gridView.setStripeRows(true);
        gridView.addColumnResizeHandler(this::changeColumnWidth);

        GridSelectionModel<Integer> sm = new GridSelectionModel<>();
        sm.addSelectionChangedHandler(this::changeRowSelection);

        grid = new Grid<Integer>(store, columns.buildColumnModel()) {
            @Override
            protected void onAttach() {
                super.onAttach();
                subscriptions.add(viewModel.getEffectiveTable().subscribe(observable -> {
                    if(observable.isLoaded()) {
                        updateView(observable.get());
                    }
                }));
                subscriptions.add(viewModel.getColumnSet().subscribe(observable -> updateColumnView(observable)));
                subscriptions.add(viewModel.getSelectedRowIndex().subscribe(observable -> updateSelection(observable)));
                TableGrid.this.loader.load(0, gridView.getCacheSize());
            }

            @Override
            protected void onDetach() {
                super.onDetach();
                subscriptions.unsubscribeAll();
            }
        };

        MenuItem editRecord = new MenuItem(I18N.CONSTANTS.editRecord(),
                selectionEvent -> tableUpdater.editSelection());

        MenuItem deleteRecord = new MenuItem(I18N.CONSTANTS.deleteRecord());

        Menu contextMenu = new Menu(new Css3MenuAppearance(MenuArrow.NONE));
        contextMenu.add(editRecord);
        contextMenu.add(deleteRecord);

        grid.setLoader(loader);
        grid.setLoadMask(true);
        grid.setView(gridView);
        grid.setSelectionModel(sm);
        grid.addSortChangeHandler(this::changeSort);
        grid.setContextMenu(contextMenu);
        grid.addBeforeShowContextMenuHandler(event -> {
            boolean hasSelection = !grid.getSelectionModel().getSelectedItems().isEmpty();
            editRecord.setEnabled(hasSelection);
            deleteRecord.setEnabled(hasSelection);
        });

        grid.addRowClickHandler(event -> {
            tableUpdater.expandRecordPanel(true);
        });

        grid.addViewReadyHandler(event -> {
            if(pendingColumnModel != null) {
                grid.reconfigure(store, pendingColumnModel);
                pendingColumnModel = null;
            }
        });
    }


    private void changeColumnWidth(ColumnResizeEvent e) {

        ColumnConfig<Integer, Object> column = grid.getColumnModel().getColumn(e.getColumnIndex());

        LOGGER.info("Column " + column.getValueProvider().getPath() + " resized to " + e.getColumnWidth() + "px");

        tableUpdater.updateColumnWidth(column.getValueProvider().getPath(), e.getColumnWidth());
    }

    /**
     * Changes the current row selection to the user's new selection
     * @param event
     */
    private void changeRowSelection(SelectionChangedEvent<Integer> event) {
        if(proxy.isLoaded()) {

            if(event.getSelection().isEmpty()) {
                // The LiveGrid clears the store when the set of loaded rows is refreshed.
                // This has the side effect of clearing the selection, which we do not want.
                // So if the grid decides to clear the selection but our state still has one, fix the grid
                Observable<Optional<Integer>> selectedIndex = viewModel.getSelectedRowIndex();
                selectedIndex.ifLoaded(optional -> {
                    optional.ifPresent(index -> {
//                        com.google.gwt.core.client.Scheduler.get().scheduleDeferred(() -> {
                            grid.getSelectionModel().setSelection(Collections.singletonList(index));
//                        });
                    });
                });

            } else {
                // This is fired in response to a user event, so update our state
                int rowIndex = event.getSelection().get(0);
                RecordRef selectedRef = new RecordRef(viewModel.getFormId(), proxy.getRecordId(rowIndex));
                tableUpdater.selectRecord(selectedRef);
            }
        }
    }

    /**
     * Changes the current sort order based on the user's input.
     */
    private void changeSort(SortChangeEvent event) {
        // TODO
    }

    public void updateView(EffectiveTableModel tableModel) {

        // Check to see if we can update columns in place
        if (tryUpdateColumnsView(tableModel)) {
            return;
        }

        LOGGER.info("Columns have changed, rebuild required.");

        ColumnModelBuilder builder = new ColumnModelBuilder(proxy);
        builder.addAll(tableModel.getColumns());

        if(grid.isViewReady()) {
            grid.reconfigure(store, builder.buildColumnModel());
        } else {
            pendingColumnModel = builder.buildColumnModel();
        }

        currentTableModel = tableModel;
    }

    private boolean tryUpdateColumnsView(EffectiveTableModel tableModel) {

        if(currentTableModel == null) {
            return false;
        }

        if(tableModel.getColumns().size() != currentTableModel.getColumns().size()) {
            return false;
        }
        for (int i = 0; i < tableModel.getColumns().size(); i++) {
            EffectiveTableColumn initialColumn = currentTableModel.getColumns().get(i);
            EffectiveTableColumn updatedColumn = tableModel.getColumns().get(i);

            // Check for incompatible changes. Changing the id or the
            // the type of column will rebuilding the grid.
            if (!initialColumn.getId().equals(updatedColumn.getId()) ||
                !initialColumn.getType().equals(updatedColumn.getType())) {
                return false;
            }
            if(!initialColumn.getLabel().equals(updatedColumn.getLabel())) {
                // TODO: update column label in place
                return false;
            }
        }


        currentTableModel = tableModel;

        return true;
    }


    private void updateSelection(Observable<Optional<Integer>> observable) {
        if(observable.isLoaded()) {
            Optional<Integer> selectedRow = observable.get();
            if(selectedRow.isPresent()) {
                grid.getSelectionModel().setSelection(Collections.singletonList(selectedRow.get()));
            } else {
                grid.getSelectionModel().setSelection(Collections.emptyList());
            }
        }
    }

    private void updateColumnView(Observable<ColumnSet> columnSet) {
        if(columnSet.isLoaded()) {
            LOGGER.info("TableGrid " + this.viewModel.getFormId() + ": ColumnSet updated");
            if(!proxy.push(columnSet.get())) {
                LOGGER.info("TableGrid " + this.viewModel.getFormId() + ": Executing loader.load()");
                loader.load();
            }
        }
    }

    @Override
    public Widget asWidget() {
        return grid;
    }


    public void setPixelSize(int width, int height) {
        grid.setPixelSize(width, height);
    }
}
