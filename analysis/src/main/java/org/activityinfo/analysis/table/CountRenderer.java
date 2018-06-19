package org.activityinfo.analysis.table;

import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;

public class CountRenderer implements ColumnRenderer<Integer> {

    private final String columnId;
    private ColumnView countColumn;

    public CountRenderer(String columnId) {
        this.columnId = columnId;
    }

    @Override
    public Integer render(int rowIndex) {
        return (int)countColumn.getDouble(rowIndex);
    }

    @Override
    public void updateColumnSet(ColumnSet columnSet) {
        countColumn = columnSet.getColumnView(columnId);
    }
}
