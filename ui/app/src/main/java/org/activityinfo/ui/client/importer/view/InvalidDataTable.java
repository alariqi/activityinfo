package org.activityinfo.ui.client.importer.view;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.datatable.DataTable;
import org.activityinfo.ui.client.base.datatable.DataTableColumn;
import org.activityinfo.ui.client.base.datatable.RowRange;
import org.activityinfo.ui.client.base.datatable.TableSlice;
import org.activityinfo.ui.client.importer.viewModel.ColumnStatus;
import org.activityinfo.ui.client.importer.viewModel.ImportedTable;
import org.activityinfo.ui.client.importer.viewModel.ValidatedColumn;
import org.activityinfo.ui.client.importer.viewModel.Validation;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidDataTable {

    public static VTree render(ImportedTable importedTable) {

        List<ValidatedColumn> mappedColumns = importedTable.getValidatedTable().getColumns()
                .stream()
                .filter(column -> column.getStatus() == ColumnStatus.MAPPED)
                .collect(Collectors.toList());

        return new DataTable()
                .setColumns(columnHeaders(mappedColumns))
                .setRowRenderer(rowRange -> renderRows(importedTable, mappedColumns, rowRange))
                .setColumnMenuVisible(false)
                .build();
    }


    private static Observable<List<DataTableColumn>> columnHeaders(List<ValidatedColumn> mappedColumns) {

        List<DataTableColumn> columns = mappedColumns
                .stream()
                .map(column -> MatchTableView.dataTableColumn(column.getColumn(), false))
                .collect(Collectors.toList());

        return Observable.just(columns);
    }

    private static Observable<TableSlice> renderRows(ImportedTable table, List<ValidatedColumn> columns, Observable<RowRange> rowRange) {

        int numRows = table.getInvalidRecordCount();
        int numCols = columns.size();

        String[] columnIds = columns.stream().map(c -> c.getId()).toArray(size -> new String[size]);
        ColumnView[] columnViews = columns.stream().map(c -> c.getColumnView()).toArray(size -> new ColumnView[size]);
        Validation[] validation = columns.stream().map(c -> c.getValidation()).toArray(size -> new Validation[size]);
        int[] invalidMap = table.getValidRowSet().buildInvalidMap();

        VTree emptyCell = new VNode(HtmlTag.TD, Props.create().setClass("unset"));

        return rowRange.transform(r -> {

            int startIndex = r.getStartIndex();
            int endIndex = Math.min(numRows, startIndex + r.getRowCount());
            int rowCount = (endIndex - startIndex);
            VTree[] rows = new VTree[rowCount];


            for (int i = 0; i < rowCount; i++) {
                int rowIndex = invalidMap[startIndex + i];
                String rowId = "" + rowIndex;

                PropMap rowProps = Props.create();
                rowProps.setData("row", rowId);

                VTree[] cells = new VTree[numCols];
                for (int j = 0; j < numCols; j++) {

                    String value = columnViews[j].getString(rowIndex);
                    if(value == null) {
                        cells[j] = emptyCell;
                    } else {
                        String classes = "unset";
                        int validCode = validation[j].getRowStatus(rowIndex);
                        if (validCode == Validation.INVALID) {
                            classes += " invalid";
                        }
                        PropMap cellProps = Props.create();
                        cellProps.setData("column", columnIds[j]);
                        cellProps.setClass(classes);

                        cells[j] = new VNode(HtmlTag.TD, cellProps, new VText(value));
                    }
                }
                rows[i] = new VNode(HtmlTag.TR, rowProps, cells, rowId);
            }

            return new TableSlice(startIndex, numRows,
                    new VNode(HtmlTag.TBODY, rows));
        });
    }

}
