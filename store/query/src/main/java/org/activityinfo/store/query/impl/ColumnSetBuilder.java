package org.activityinfo.store.query.impl;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.activityinfo.model.expr.diagnostic.ExprException;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.FormTreeBuilder;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.query.QuerySyntaxException;
import org.activityinfo.store.query.impl.eval.QueryEvaluator;
import org.activityinfo.store.spi.FormCatalog;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class ColumnSetBuilder {

    public static final Logger LOGGER = Logger.getLogger(ColumnSetBuilder.class.getName());

    private final FormCatalog catalog;
    private final FormTreeBuilder formTreeBuilder;
    private final FormSupervisor supervisor;
    private FormScanCache cache;
    private Function<ColumnView, ColumnView> filter;
    private Map<String, Slot<ColumnView>> columnViews;
    private Slot<ColumnView> columnForRowCount;

    public ColumnSetBuilder(FormCatalog catalog, FormScanCache cache, FormSupervisor supervisor) {
        this.catalog = catalog;
        this.formTreeBuilder = new FormTreeBuilder(catalog);
        this.cache = cache;
        this.supervisor = supervisor;
    }

    public ColumnSetBuilder(FormCatalog catalog) {
        this(catalog, new AppEngineFormScanCache(), new NullFormSupervisor());
    }

    public ColumnSetBuilder(FormCatalog formCatalog, FormScanCache formScanCache) {
        this(formCatalog, formScanCache, new NullFormSupervisor());
    }

    public ColumnSet build(QueryModel queryModel) {

        // We want to make at most one pass over every collection we need to scan,
        // so first queue up all necessary work before executing
        FormScanBatch batch = new FormScanBatch(new FormStorageScanner(catalog), cache);

        // Enqueue the columns we need
        enqueue(queryModel, batch);

        // Now execute the batch
        try {
            batch.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute query batch", e);
        }

        return build();
    }


    public void enqueue(QueryModel table, FormScanBatch batch) {
        ResourceId formId = table.getRowSources().get(0).getRootFormId();
        FormTree tree = formTreeBuilder.queryTree(formId);

        FormClass formClass = tree.getRootFormClass();
        Preconditions.checkNotNull(formClass);

        FormScanBatch filteredBatch = new FormScanBatch(new FilteredScanner(tree, batch, supervisor));
        QueryEvaluator evaluator = new QueryEvaluator(tree, filteredBatch);

        filter = evaluator.filter(table.getFilter());

        columnViews = Maps.newHashMap();
        for(ColumnModel column : table.getColumns()) {
            Slot<ColumnView> view;
            try {
                view = evaluator.evaluateExpression(column.getExpression());
            } catch(ExprException e) {
                throw new QuerySyntaxException("Syntax error in column " + column.getId() +
                        " '" + column.getExpression() + "' : " + e.getMessage(), e);
            }
            columnViews.put(column.getId(), view);
        }

        columnForRowCount = null;

        if(columnViews.isEmpty()) {
            // handle empty queries as a special case: we still need the
            // row count so query the id
            columnForRowCount = batch.addResourceIdColumn(formClass);
        }
    }

    public ColumnSet build() {

        // handle the special case of no columns
        if(columnViews.isEmpty()) {
            int numRows = columnForRowCount.get().numRows();
            Map<String, ColumnView> columns = Collections.emptyMap();
            return new ColumnSet(numRows, columns);
        }

        // Otherwise resolve the columns, filter, and package the
        // result
        Map<String, ColumnView> dataMap = Maps.newHashMap();
        for (Map.Entry<String, Slot<ColumnView>> entry : columnViews.entrySet()) {
            ColumnView view = filter.apply(entry.getValue().get());

            dataMap.put(entry.getKey(), view);
        }

        return new ColumnSet(commonLength(dataMap), dataMap);
    }

    private static int commonLength(Map<String, ColumnView> dataMap) {
        Iterator<ColumnView> iterator = dataMap.values().iterator();
        if(!iterator.hasNext()) {
            throw new IllegalStateException("Cannot calculate row count from empty column set.");
        }

        int length = iterator.next().numRows();
        while(iterator.hasNext()) {
            if(length != iterator.next().numRows()) {
                logMismatchedRows(dataMap);
                throw new IllegalStateException("Query returned columns of different lengths. See logs for details.");
            }
        }
        return length;
    }

    private static void logMismatchedRows(Map<String, ColumnView> dataMap) {
        StringBuilder message = new StringBuilder();
        message.append("Query returned columns of different lengths:");
        for (Map.Entry<String, ColumnView> entry : dataMap.entrySet()) {
            message.append("\n").append(entry.getKey()).append(" = ").append(entry.getValue().numRows());
        }
        LOGGER.severe(message.toString());
    }
}
