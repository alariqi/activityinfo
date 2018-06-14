package org.activityinfo.ui.client.table.view;

import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public interface ValueRenderer {

    VTree render(RecordTree recordTree, FieldValue fieldValue);
}
