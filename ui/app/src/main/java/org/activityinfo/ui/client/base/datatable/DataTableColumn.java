package org.activityinfo.ui.client.base.datatable;

import org.activityinfo.model.annotation.AutoBuilder;
import org.activityinfo.ui.vdom.shared.tree.VTree;

@AutoBuilder
public class DataTableColumn {
    int width;
    VTree header;
    boolean filterEnabled;

    DataTableColumn() {
    }

    public int getWidth() {
        return width;
    }

    public VTree getHeader() {
        return header;
    }

    public boolean isFilterEnabled() {
        return filterEnabled;
    }
}
