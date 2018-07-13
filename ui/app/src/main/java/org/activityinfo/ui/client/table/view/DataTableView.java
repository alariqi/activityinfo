package org.activityinfo.ui.client.table.view;


import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.datatable.DataTable;
import org.activityinfo.ui.client.table.state.TableUpdater;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class DataTableView {

    public static VTree render(TableViewModel viewModel, TableUpdater updater) {

        Observable<DataTableRenderer> renderer =
                viewModel.getEffectiveTable().transform(table -> new DataTableRenderer(table));

        Observable<ColumnSet> columnSet = viewModel.getColumnSet();

        return new DataTable()
                .setColumns(renderer.transform(r -> r.getColumns()))
                .setRowRenderer(range -> renderer.join(r -> r.renderRows(columnSet, range)))
                .setRowClickHandler(rowId -> {
                    RecordRef ref = new RecordRef(viewModel.getFormId(), ResourceId.valueOf(rowId));
                    updater.update(s -> s.withSelection(ref).withRecordPanelExpanded(true));
                })
                .setSelectedRow(viewModel.getSelectedRecordRef().transform(o ->
                        o.map(ref -> ref.getRecordId().asString()).orElse("")))
                .build();

    }



}
