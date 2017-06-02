package org.activityinfo.store.query.impl;

import org.activityinfo.model.expr.ExprNode;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.store.query.impl.join.ForeignKeyMap;
import org.activityinfo.store.query.impl.join.PrimaryKeyMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 2-6-17.
 */
public interface FormScan {
    Slot<ColumnView> addResourceId();

    Slot<PrimaryKeyMap> addPrimaryKey();

    Slot<Integer> addCount();

    Slot<ColumnView> addField(ExprNode fieldExpr);

    Slot<ForeignKeyMap> addForeignKey(String fieldName);

    Slot<ForeignKeyMap> addForeignKey(ExprNode referenceField);

    Set<String> getCacheKeys();

    void updateFromCache(Map<String, Object> cached);

    void execute() throws Exception;

    Map<String, Object> getValuesToCache();
}
