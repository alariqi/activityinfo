package org.activityinfo.ui.client.table.view.columns;

import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class DoubleRenderer implements ColumnRenderer {

    private final String id;

    private ColumnView columnView;

    public DoubleRenderer(String id) {
        this.id = id;
    }

    @Override
    public void init(ColumnSet columnSet) {
        columnView = columnSet.getColumnView(id);
    }

    @Override
    public VTree renderCell(int row) {
        double value = columnView.getDouble(row);
        String string;
        if(Double.isNaN(value)) {
            string = "";
        } else {
            string = "" + value;
        }
        return new VNode(HtmlTag.TD, new VText(string));
    }
}
