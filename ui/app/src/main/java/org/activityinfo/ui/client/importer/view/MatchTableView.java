package org.activityinfo.ui.client.importer.view;

import com.google.common.base.Strings;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.datatable.*;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceViewModel;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

public class MatchTableView {


    public static VTree render(ImportViewModel viewModel, ImportUpdater updater) {

        return new DataTable()
                .setColumns(viewModel.getSource().transform(MatchTableView::columnHeaders))
                .setRowRenderer(rowRange -> renderRows(viewModel, rowRange))
                .build();

    }


    private static List<DataTableColumn> columnHeaders(SourceViewModel sourceViewModel) {
        List<DataTableColumn> columns = new ArrayList<>();
        for (SourceColumn sourceColumn : sourceViewModel.getColumns()) {
            columns.add(new DataTableColumnBuilder()
                .setHeading(sourceColumn.getLabel())
                .setWidth(150)
                .setSurtitle("Unset")
                .build());
        }
        return columns;
    }


    private static Observable<TableSlice> renderRows(ImportViewModel viewModel, Observable<RowRange> rowRange) {

        return Observable.transform(viewModel.getSource(), rowRange, (source, r) -> {
                int numRows = source.getRowCount();
                int numCols = source.getColumns().size();

                int startIndex = r.getStartIndex();
                int endIndex = Math.min(numRows, startIndex + r.getRowCount());
                int rowCount = (endIndex - startIndex);
                VTree[] rows = new VTree[rowCount];

                for (int i = 0; i < rowCount; i++) {
                    int rowIndex = startIndex + i;
                    String rowId = "" + rowIndex;

                    PropMap rowProps = Props.create();
                    rowProps.setData("row", rowId);

                    VTree[] cells = new VTree[numCols];
                    for (int j = 0; j < numCols; j++) {
                        String value = source.getColumns().get(j).getColumnView().getString(rowIndex);
                        if(Strings.isNullOrEmpty(value)) {
                            cells[j] = new VNode(HtmlTag.TD, PropMap.EMPTY);
                        } else {
                            cells[j] = new VNode(HtmlTag.TD, PropMap.EMPTY, new VText(value));
                        }
                    }
                    rows[i] = new VNode(HtmlTag.TR, rowProps, cells, rowId);
                }

                return new TableSlice(startIndex, numRows,
                        new VNode(HtmlTag.TBODY, rows));
            });
    }
}
