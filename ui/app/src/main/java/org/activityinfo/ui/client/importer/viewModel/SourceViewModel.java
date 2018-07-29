package org.activityinfo.ui.client.importer.viewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SourceViewModel {

    private List<SourceColumn> columns = new ArrayList<>();

    public SourceViewModel() {
    }

    public SourceViewModel(List<SourceColumn> columns) {
        this.columns = columns;
    }

    public boolean isValid() {
        return columns.size() > 0;
    }

    public List<SourceColumn> getColumns() {
        return columns;
    }

    public SourceColumn getColumn(int columnIndex) {
        return columns.get(columnIndex);
    }

    public int getRowCount() {
        if(columns.isEmpty()) {
            return 0;
        } else {
            return columns.get(0).getColumnView().numRows();
        }
    }

    public boolean hasColumnId(String id) {
        return getColumnIndex(id) != -1;
    }

    public int getColumnIndex(String columnId) {
        for (int i = 0; i < columns.size(); i++) {
            if(columns.get(i).getId().equals(columnId)) {
                return i;
            }
        }
        return -1;
    }

    public Optional<SourceColumn> getColumnById(String id) {
        int index = getColumnIndex(id);
        if(index < 0) {
            return Optional.empty();
        } else {
            return Optional.of(columns.get(index));
        }
    }
}
