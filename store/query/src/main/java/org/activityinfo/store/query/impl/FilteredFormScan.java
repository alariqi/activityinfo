package org.activityinfo.store.query.impl;

import org.activityinfo.model.expr.ExprNode;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.store.query.impl.builders.PrimaryKeySlot;
import org.activityinfo.store.query.impl.join.ForeignKeyMap;
import org.activityinfo.store.query.impl.join.PrimaryKeyMap;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


public class FilteredFormScan implements FormScan {

    private static final Logger LOGGER = Logger.getLogger(FilteredFormScan.class.getName());

    private final FormScan delegate;
    private final Slot<ColumnView> filter;

    private PrimaryKeySlot primaryKeySlot;

    public FilteredFormScan(FormScan delegate, Slot<ColumnView> filter) {
        this.delegate = delegate;
        this.filter = filter;
    }

    private Slot<ColumnView> filter(Slot<ColumnView> view) {
        return view;
    }

    @Override
    public Slot<ColumnView> addResourceId() {
        return filter(delegate.addResourceId());
    }


    @Override
    public Slot<PrimaryKeyMap> addPrimaryKey() {
        if(primaryKeySlot == null) {
            primaryKeySlot = new PrimaryKeySlot(addResourceId());
        }
        return primaryKeySlot;
    }

    @Override
    public Slot<Integer> addCount() {
        return delegate.addCount();
    }

    @Override
    public Slot<ColumnView> addField(ExprNode fieldExpr) {
        return delegate.addField(fieldExpr);
    }

    @Override
    public Slot<ForeignKeyMap> addForeignKey(String fieldName) {
        return delegate.addForeignKey(fieldName);
    }

    @Override
    public Slot<ForeignKeyMap> addForeignKey(ExprNode referenceField) {
        return delegate.addForeignKey(referenceField);
    }

    @Override
    public Set<String> getCacheKeys() {
        return delegate.getCacheKeys();
    }

    @Override
    public void updateFromCache(Map<String, Object> cached) {
        delegate.updateFromCache(cached);
    }

    @Override
    public void execute() throws Exception {
        delegate.execute();
    }

    @Override
    public Map<String, Object> getValuesToCache() {
        return delegate.getValuesToCache();
    }
}
