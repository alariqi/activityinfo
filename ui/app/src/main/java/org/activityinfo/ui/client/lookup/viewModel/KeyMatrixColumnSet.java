package org.activityinfo.ui.client.lookup.viewModel;

import org.activityinfo.model.formTree.LookupKey;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;

public class KeyMatrixColumnSet {
    private ResourceId formId;
    private ColumnSet columnSet;

    KeyMatrixColumnSet(ResourceId formId, ColumnSet columnSet) {
        this.formId = formId;
        this.columnSet = columnSet;
    }

    public ResourceId getFormId() {
        return formId;
    }

    public ColumnView getId() {
        return columnSet.getColumnView(KeyMatrix.ID_COLUMN);
    }

    public ColumnView getKey(LookupKey key) {
        return columnSet.getColumnView(KeyMatrix.keyColumn(key));
    }
}
