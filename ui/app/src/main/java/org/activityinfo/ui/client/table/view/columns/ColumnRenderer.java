package org.activityinfo.ui.client.table.view.columns;

import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public interface ColumnRenderer {

    void init(ColumnSet columnSet);

    VTree renderCell(int row);
}
