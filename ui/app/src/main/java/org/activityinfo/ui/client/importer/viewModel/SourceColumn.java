package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.ColumnView;

public class SourceColumn {
    private String label;
    private ColumnView columnView;


    public SourceColumn(String label, ColumnView columnView) {
        this.label = label;
        this.columnView = columnView;
    }

    public String getLabel() {
        return label;
    }

    public ColumnView getColumnView() {
        return columnView;
    }
}
