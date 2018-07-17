package org.activityinfo.ui.client.importer.view;

import com.google.common.base.Strings;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.datatable.*;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceViewModel;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

public class MatchTableView {


    public static VTree render(ImportViewModel viewModel, ImportUpdater updater) {

        return new DataTable()
                .setColumns(columnHeaders(viewModel))
                .setRowRenderer(rowRange -> renderRows(viewModel, rowRange))
                .setColumnClickHandler(columnId -> updater.update(s -> s.selectColumn(columnId)))
                .setColumnMenuVisible(false)
                .build();
    }


    private static Observable<List<DataTableColumn>> columnHeaders(ImportViewModel viewModel) {
        Observable<String> selectedColumnId = viewModel.getSelectedColumnId();
        Observable<MappedSourceViewModel> source = viewModel.getMappedSource();

        return Observable.transform(source, selectedColumnId, (s, selectedId) -> {
            List<DataTableColumn> columns = new ArrayList<>();
            for (MappedSourceColumn column : s.getColumns()) {
                columns.add(new DataTableColumnBuilder()
                        .setId(column.getId())
                        .setHeading(column.getLabel())
                        .setHeadingClass(headerClass(column))
                        .setWidth(150)
                        .setColumnSelected(column.getId().equals(selectedId))
                        .setSurtitle(column.getStatusLabel())
                        .build());
            }
            return columns;
        });

    }

    private static String headerClass(MappedSourceColumn column) {
        switch (column.getStatus()) {
            case UNSET:
                return "unset";
            case IGNORED:
                return "ignored";
            default:
                return "";
        }
    }


    private static Observable<TableSlice> renderRows(ImportViewModel viewModel, Observable<RowRange> rowRange) {

        return Observable.transform(viewModel.getSource(), rowRange, viewModel.getSelectedColumnId(),
                (source, r, selectedColumnId) -> {

                    int numRows = source.getRowCount();
                    int numCols = source.getColumns().size();

                    int startIndex = r.getStartIndex();
                    int endIndex = Math.min(numRows, startIndex + r.getRowCount());
                    int rowCount = (endIndex - startIndex);
                    VTree[] rows = new VTree[rowCount];

                    String columnIds[] = new String[numCols];
                    for (int i = 0; i < numCols; i++) {
                        columnIds[i] = source.getColumns().get(i).getId();
                    }

                    int selectedColumnIndex = source.getColumnIndex(selectedColumnId);

                    for (int i = 0; i < rowCount; i++) {
                        int rowIndex = startIndex + i;
                        String rowId = "" + rowIndex;

                        PropMap rowProps = Props.create();
                        rowProps.setData("row", rowId);

                        VTree[] cells = new VTree[numCols];
                        for (int j = 0; j < numCols; j++) {
                            PropMap cellProps = Props.create();
                            cellProps.setData("column", columnIds[j]);
                            if(j == selectedColumnIndex) {
                                cellProps.addClassName("selected");
                            }
                            String value = source.getColumns().get(j).getColumnView().getString(rowIndex);
                            cells[j] = new VNode(HtmlTag.TD, cellProps,
                                    Strings.isNullOrEmpty(value) ?
                                            VNode.NO_CHILDREN :
                                            new VTree[] { new VText(value) });
                        }
                        rows[i] = new VNode(HtmlTag.TR, rowProps, cells, rowId);
                    }

                    return new TableSlice(startIndex, numRows,
                            new VNode(HtmlTag.TBODY, rows));
                });
    }
}
