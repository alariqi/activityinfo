package org.activityinfo.ui.client.importer.view;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.datatable.*;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.*;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

public class MatchTableView {

    private static final String VALIDATION_CLASSES[] = new String[] { "", "invalid", "validating" };

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

        return Observable.transform(viewModel.getValidatedTable(), rowRange, viewModel.getSelectedColumnId(),
                (source, r, selectedColumnId) -> {

                    int numRows = source.getNumRows();
                    int numCols = source.getNumColumns();

                    int startIndex = r.getStartIndex();
                    int endIndex = Math.min(numRows, startIndex + r.getRowCount());
                    int rowCount = (endIndex - startIndex);
                    VTree[] rows = new VTree[rowCount];

                    String columnIds[] = source.getColumnIds();

                    ColumnView[] columnViews = source.getColumnViews();
                    Validation[] validation = source.getValidationArray();

                    // Compute the styles common to all column cells
                    String[] columnStyles = computeColumnStyles(source, selectedColumnId);
                    VTree[] emptyNodes = computeEmptyNodes(source, columnStyles);

                    for (int i = 0; i < rowCount; i++) {
                        int rowIndex = startIndex + i;
                        String rowId = "" + rowIndex;

                        PropMap rowProps = Props.create();
                        rowProps.setData("row", rowId);

                        VTree[] cells = new VTree[numCols];
                        for (int j = 0; j < numCols; j++) {

                            String value = columnViews[j].getString(rowIndex);
                            if(value == null) {

                                cells[j] = emptyNodes[j];

                            } else {
                                String classes = columnStyles[j];
                                int validCode = validation[j].getRowStatus(rowIndex);
                                if (validCode > 0) {
                                    classes += VALIDATION_CLASSES[validCode];
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

    private static String[] computeColumnStyles(ValidatedTable source, String selectedColumnId) {
        String[] columnStyles = new String[source.getNumColumns()];

        for (int i = 0; i < source.getNumColumns(); i++) {
            ValidatedColumn column = source.getColumns().get(i);
            String classes = "";
            if(column.getId().equals(selectedColumnId)) {
                classes += "selected ";
            }
            if(column.getStatus() == ColumnStatus.IGNORED) {
                classes += "ignored ";
            }
            if(column.getStatus() == ColumnStatus.UNSET) {
                classes += "unset ";
            }
            columnStyles[i] = classes;
        }
        return columnStyles;
    }


    private static VTree[] computeEmptyNodes(ValidatedTable source, String[] columnStyles) {
        VTree[] nodes = new VTree[source.getNumColumns()];
        for (int i = 0; i < nodes.length; i++) {
            ValidatedColumn column = source.getColumns().get(i);

            PropMap cellProps = Props.create();
            cellProps.setData("column", column.getId());
            cellProps.setClass(columnStyles[i]);

            nodes[i] = new VNode(HtmlTag.TD, cellProps);
        }
        return nodes;
    }

}
