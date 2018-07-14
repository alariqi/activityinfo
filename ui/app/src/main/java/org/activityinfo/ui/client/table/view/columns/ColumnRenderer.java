package org.activityinfo.ui.client.table.view.columns;

import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public interface ColumnRenderer {

    VNode EMPTY_CELL = new VNode(HtmlTag.TD);

    void init(ColumnSet columnSet);

    VTree renderCell(int row);
}
