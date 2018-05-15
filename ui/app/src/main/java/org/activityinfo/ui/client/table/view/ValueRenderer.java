package org.activityinfo.ui.client.table.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.type.FieldValue;

public interface ValueRenderer {

    SafeHtml render(RecordTree recordTree, FieldValue fieldValue);
}
