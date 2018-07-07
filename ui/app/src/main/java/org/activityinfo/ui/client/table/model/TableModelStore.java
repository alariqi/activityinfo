package org.activityinfo.ui.client.table.model;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds, caches, and updates table models.
 */
public class TableModelStore {

    public static TableModelStore STORE = new TableModelStore();

    private Map<ResourceId, StatefulValue<TableModel>> tableMap = new HashMap<>();

    private TableModelStore() {
    }

    public Observable<TableModel> getTableModel(ResourceId formId) {
        return getStatefulTableModel(formId);
    }

    private StatefulValue<TableModel> getStatefulTableModel(ResourceId formId) {
        return tableMap.computeIfAbsent(formId, id -> new StatefulValue<>(new TableModel(id)));
    }

}
