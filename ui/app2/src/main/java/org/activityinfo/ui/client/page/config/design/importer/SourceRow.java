package org.activityinfo.ui.client.page.config.design.importer;

import java.util.ArrayList;
import java.util.List;

public class SourceRow {

    int rowIndex;
    List<String> columnValues = new ArrayList<>();

    public SourceRow(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getColumnValue(int index) {
        if(index < columnValues.size()) {
            return columnValues.get(index);
        } else {
            return "";
        }
    }

    public int getRowIndex() {
        return rowIndex;
    }
}
