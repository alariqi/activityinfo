package org.activityinfo.ui.client.analysis.view;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.*;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.analysis.viewModel.AnalysisViewModel;
import org.activityinfo.ui.client.analysis.viewModel.EffectiveDimension;
import org.activityinfo.ui.client.analysis.viewModel.PivotTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class PivotTableView implements IsWidget {

    private static final Logger LOGGER = Logger.getLogger(PivotTableView.class.getName());

    private final AnalysisViewModel model;
    private ListStore<PivotRow> store;
    private ContentPanel panel;
    private Grid<PivotRow> grid;
    private final TextButton saveButton;

    public PivotTableView(AnalysisViewModel model) {
        this.model = model;
        this.store = new ListStore<>(point -> point.toString());

        saveButton = new TextButton(I18N.CONSTANTS.save());

        ToolBar toolbar = new ToolBar();
        toolbar.add(saveButton);

        this.grid = new Grid<>(store, buildColumnModel(new PivotTable()));
        this.grid.getView().setSortingEnabled(false);
        this.grid.setSelectionModel(new CellSelectionModel<>());

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(toolbar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(grid, new VerticalLayoutContainer.VerticalLayoutData(-1, -1));

        this.panel = new ContentPanel();
        this.panel.setHeading("Results");
        this.panel.add(container);

        model.getPivotTable().subscribe(observable -> {
            if (observable.isLoaded()) {
                update(observable.get());
            } else {
                store.clear();
            }
        });
    }

    public TextButton getSaveButton() {
        return saveButton;
    }

    private void update(PivotTable pivotTable) {
        grid.reconfigure(store, buildColumnModel(pivotTable));
        store.replaceAll(buildRows(pivotTable));
    }

    private ColumnModel<PivotRow> buildColumnModel(PivotTable pivotTable) {

        List<ColumnConfig<PivotRow, ?>> columns = new ArrayList<>();

        List<EffectiveDimension> rowDimensions = pivotTable.getRowDimensions();
        for (int i = 0; i < rowDimensions.size(); i++) {
            EffectiveDimension rowDim = rowDimensions.get(i);
            ColumnConfig<PivotRow, String> column = new ColumnConfig<>(new PivotRowHeaderProvider(i));
            column.setSortable(false);
            column.setHideable(false);
            column.setHeader(rowDim.getLabel());
            column.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
            columns.add(column);
        }

        List<PivotTable.Node> leafColumns = pivotTable.getRootColumn().getLeaves();
        for (PivotTable.Node leafColumn : leafColumns) {
            ColumnConfig<PivotRow, String> column = new ColumnConfig<>(new PivotValueProvider(leafColumn));
            if(leafColumn.getCategoryLabel() == null) {
                column.setHeader(I18N.CONSTANTS.value());
            } else {
                column.setHeader(leafColumn.getCategoryLabel());
            }
            column.setSortable(false);
            column.setHideable(false);
            column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_END);
            columns.add(column);
        }

        ColumnModel<PivotRow> columnModel = new ColumnModel<>(columns);

        if(pivotTable.getColumnDimensions().size() > 0) {
            int startRow = 0;
            int startCol = pivotTable.getRowDimensions().size();
            addHeaderGroups(columnModel, pivotTable.getRootColumn(), startRow, startCol);
        }
        return columnModel;
    }

    private int addHeaderGroups(ColumnModel<PivotRow> cm, PivotTable.Node parent, int row, int col) {
        int leafCount = parent.getLeaves().size();

        // Add one group for the name of this Dimension
        cm.addHeaderGroup(row, col, new HeaderGroupConfig(parent.getDimension().getLabel(), 1, leafCount));


        for (PivotTable.Node child : parent.getChildren()) {
            if (child.isLeaf()) {
                col++;
            } else {
                cm.addHeaderGroup(row + 1, col,
                        new HeaderGroupConfig(child.getCategoryLabel(), 1, child.getLeaves().size()));
                col = addHeaderGroups(cm, child, row + 2, col);
            }
        }
        return col;
    }

    private List<PivotRow> buildRows(PivotTable table) {
        List<PivotRow> list = new ArrayList<>();
        String[] rowHeaders = new String[table.getRowDimensions().size()];

        addRows(list, table.getRootRow(), rowHeaders, 0);

        return list;

    }

    private void addRows(List<PivotRow> list, PivotTable.Node parent, String[] rowHeaders, int rowHeaderIndex) {

        if(parent.isLeaf()) {
            list.add(new PivotRow(Arrays.copyOf(rowHeaders, rowHeaders.length), parent));
        } else {
            for (PivotTable.Node child : parent.getChildren()) {
                rowHeaders[rowHeaderIndex] = child.getCategoryLabel();
                addRows(list, child, rowHeaders, rowHeaderIndex+1);
                if(rowHeaderIndex > 0) {
                    rowHeaders[rowHeaderIndex - 1] = null;
                }
            }
        }
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
}
