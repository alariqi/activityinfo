package org.activityinfo.ui.client.table.model;

import com.google.common.base.Optional;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds, caches, and updates table models.
 */
public class TableModelStore {

    public static TableModelStore STORE = new TableModelStore();

    private Map<ResourceId, StatefulValue<TableSliderModel>> modelMap = new HashMap<>();
    private Map<ResourceId, StatefulValue<TableModel>> tableMap = new HashMap<>();

    private TableModelStore() {
    }

    public Observable<TableSliderModel> getTableTreeModel(ResourceId rootFormId) {
        StatefulValue<TableSliderModel> model = getModel(rootFormId);
        return model;
    }

    private StatefulValue<TableSliderModel> getModel(ResourceId formId) {
        return modelMap.computeIfAbsent(formId, id -> new StatefulValue<>(new TableSliderModel(id)));
    }

    public Observable<TableModel> getTableModel(ResourceId formId) {
        return getStatefulTableModel(formId);
    }

    private StatefulValue<TableModel> getStatefulTableModel(ResourceId formId) {
        return tableMap.computeIfAbsent(formId, id -> new StatefulValue<>(new TableModel(id)));
    }

    public TableUpdater getTableUpdater(ResourceId formId) {
        StatefulValue<TableModel> model = getStatefulTableModel(formId);
        return new TableUpdater() {

            @Override
            public void updateFilter(Optional<FormulaNode> filterFormula) {
                throw new UnsupportedOperationException("TODO");
            }

            @Override
            public void updateColumnWidth(String columnId, int width) {
                throw new UnsupportedOperationException("TODO");
            }

            @Override
            public void editRecord(RecordRef ref) {
                throw new UnsupportedOperationException("TODO");
            }

            @Override
            public void editSelection() {
                throw new UnsupportedOperationException("TODO");
            }

            @Override
            public void newRecord() {
                throw new UnsupportedOperationException("TODO");
            }

            @Override
            public void selectRecord(RecordRef selectedRef) {
                TableModel updatedModel = model.get().withSelection(selectedRef);
                model.updateIfNotEqual(updatedModel);
            }
        };
    }
}
