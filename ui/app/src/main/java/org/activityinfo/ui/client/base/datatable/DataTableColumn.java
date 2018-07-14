package org.activityinfo.ui.client.base.datatable;

import org.activityinfo.ui.vdom.shared.tree.VTree;

public class DataTableColumn {
    int width;
    VTree header;


    public DataTableColumn(int width, VTree header) {
        this.width = width;
        this.header = header;
    }

    public int getWidth() {
        return width;
    }

    /**
     * @return
     */
    public VTree getHeader() {
        return header;
    }

}
