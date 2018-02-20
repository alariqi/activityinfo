package org.activityinfo.analysis.table;

import org.activityinfo.model.query.ColumnView;

public class StringRenderer extends AbstractRenderer<String> {

    public StringRenderer(String columnId) {
        super(columnId);
    }

    @Override
    protected String renderRow(ColumnView view, int rowIndex) {
        return view.getString(rowIndex);
    }
}
