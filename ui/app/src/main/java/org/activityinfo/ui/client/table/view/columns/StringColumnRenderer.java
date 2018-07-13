package org.activityinfo.ui.client.table.view.columns;

import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class StringColumnRenderer implements ColumnRenderer {
    private final String id;

    private org.activityinfo.model.query.ColumnView columnView;

    public StringColumnRenderer(String id) {
        this.id = id;
    }


    @Override
    public void init(ColumnSet columnSet) {
        columnView = columnSet.getColumnView(id);
    }

    @Override
    public VTree renderCell(int row) {
        String string = columnView.getString(row);
        if(string == null) {
            string = "";
        }
        return new VNode(HtmlTag.TD, new VText(string));
    }
}
