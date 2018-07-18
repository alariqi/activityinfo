package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.ColumnView;

import java.util.List;
import java.util.Optional;

public class ValidatedTable {

    private final int numColumns;
    private final int numRows;
    private final ColumnView[] columnViews;
    private final String[] columnIds;
    private final Validation[] validation;
    private final List<ValidatedColumn> columns;

    public ValidatedTable(List<ValidatedColumn> columns) {
        this.columns = columns;
        numColumns = columns.size();
        numRows = columns.isEmpty() ? 0 : columns.get(0).getColumn().getColumn().getColumnView().numRows();

        columnViews = new ColumnView[numColumns];
        columnIds = new String[numColumns];
        validation = new Validation[numColumns];
        for (int i = 0; i < numColumns; i++) {
            SourceColumn sourceColumn = columns.get(i).getColumn().getColumn();
            columnViews[i] = sourceColumn.getColumnView();
            columnIds[i] = sourceColumn.getId();
            validation[i] = columns.get(i).getValidation();
        }
    }

    public List<ValidatedColumn> getColumns() {
        return columns;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public ColumnView[] getColumnViews() {
        return columnViews;
    }

    public String[] getColumnIds() {
        return columnIds;
    }

    public Validation[] getValidationArray() {
        return validation;
    }

    public Optional<ValidatedColumn> getColumnById(String columnId) {
        return columns.stream().filter(c -> c.getId().equals(columnId)).findAny();
    }
}
