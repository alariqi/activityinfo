package org.activityinfo.ui.client.table.view.columns;

import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnType;
import org.activityinfo.model.query.EmptyColumnView;
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
        if(columnView == null) {
            columnView = new EmptyColumnView(ColumnType.STRING, columnSet.getNumRows());
        }
    }

    @Override
    public VTree renderCell(int row) {
        String string = columnView.getString(row);
        if(string == null || string.isEmpty()) {
            return EMPTY_CELL;
        }
        return new VNode(HtmlTag.TD, new VText(string));
    }
}
