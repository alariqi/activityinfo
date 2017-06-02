package org.activityinfo.store.query.impl;

import org.activityinfo.model.expr.ExprNode;
import org.activityinfo.model.expr.ExprParser;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.query.impl.eval.QueryEvaluator;
import org.activityinfo.store.spi.FormPermissions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates scans over forms, filtered by permission visiblity filters.
 */
public class FilteredScanner implements FormScanner {

    private static final Logger LOGGER = Logger.getLogger(FilteredScanner.class.getName());

    private final FormScanBatch unfilteredBatch;
    private final QueryEvaluator unfilteredQueryEvaluator;

    private final FormSupervisor supervisor;

    public FilteredScanner(FormTree formTree, FormScanBatch unfilteredBatch, FormSupervisor supervisor) {
        this.unfilteredBatch = unfilteredBatch;
        this.unfilteredQueryEvaluator = new QueryEvaluator(formTree, unfilteredBatch);
        this.supervisor = supervisor;
    }

    @Override
    public FormScan scan(ResourceId formId) {
        FormPermissions permissions = supervisor.getFormPermissions(formId);
        if(!permissions.isVisible()) {
            return new EmptyFormScan();
        }

        if (permissions.hasVisiblityFilter()) {
            return filteredScan(formId, permissions);
        }

        return unfilteredBatch.getTable(formId);
    }

    private FormScan filteredScan(ResourceId formId, FormPermissions permissions) {

        try {
            ExprNode formula = ExprParser.parse(permissions.getVisibilityFilter());
            Slot<ColumnView> filterView = unfilteredQueryEvaluator.evaluateExpression(formula);
            FormScan unfilteredScan = unfilteredBatch.getTable(formId);

            return new FilteredFormScan(unfilteredScan, filterView);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse visibility filter", e.getMessage());
            LOGGER.severe("Error parsing visibility filter '" + permissions.getVisibilityFilter() +
                    " in form " + formId + ": " + e.getMessage() + ". " +
                    "For security reasons, no results will be shown");

            return new EmptyFormScan();
        }
    }
}
