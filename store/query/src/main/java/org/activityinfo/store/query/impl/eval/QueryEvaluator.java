package org.activityinfo.store.query.impl.eval;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.activityinfo.model.expr.*;
import org.activityinfo.model.expr.diagnostic.ExprException;
import org.activityinfo.model.expr.functions.ColumnFunction;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.type.primitive.BooleanFieldValue;
import org.activityinfo.store.query.impl.FormScanBatch;
import org.activityinfo.store.query.impl.Slot;
import org.activityinfo.store.query.impl.builders.ColumnCombiner;
import org.activityinfo.store.query.impl.builders.ConstantColumnBuilder;
import org.activityinfo.store.query.impl.views.ColumnFilter;
import org.activityinfo.store.query.shared.NodeMatch;
import org.activityinfo.store.query.shared.NodeMatcher;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Constructs a set of rows from a given RowSource model.
 *
 * Several row sources may combine to form a single logical table.
 */
public class QueryEvaluator {
    private static final Logger LOGGER = Logger.getLogger(QueryEvaluator.class.getName());

    private FormScanBatch batch;
    private FormTree tree;
    private FormClass rootFormClass;

    private final ColumnExprVisitor columnVisitor = new ColumnExprVisitor();

    private NodeMatcher resolver;

    private Deque<SymbolExpr> evaluationStack = new ArrayDeque<>();

    public QueryEvaluator(FormTree formTree, FormScanBatch batch) {
        this.tree = formTree;
        this.resolver = new NodeMatcher(formTree);
        this.rootFormClass = tree.getRootFormClass();
        this.batch = batch;
    }

    public Slot<ColumnView> evaluateExpression(ExprNode expr) {
        return expr.accept(columnVisitor);
    }


    private Slot<ColumnView> evaluateExpression(String expression) {
        ExprNode parsed = ExprParser.parse(expression);
        return parsed.accept(columnVisitor);
    }

    public Function<ColumnView, ColumnView> filter(ExprNode filter) {
        Optional<Slot<ColumnView>> parentColumn = batch.addParentMask(rootFormClass);
        if(filter == null && !parentColumn.isPresent()) {
            return Functions.identity();
        }

        Slot<ColumnView> filterView;
        if(filter == null) {
            filterView = new ConstantColumnBuilder(batch.addRowCount(rootFormClass), BooleanFieldValue.TRUE);
        } else {
            filterView = evaluateExpression(filter);
        }

        return new ColumnFilter(filterView, batch.addParentMask(rootFormClass));
    }

    private class ColumnExprVisitor implements ExprVisitor<Slot<ColumnView>> {

        @Override
        public Slot<ColumnView> visitConstant(ConstantExpr node) {
            return batch.addConstantColumn(rootFormClass, node.getValue());
        }

        @Override
        public Slot<ColumnView> visitSymbol(SymbolExpr symbolExpr) {

            // Check for recursion: are we in the process of evaluating
            // this symbol? Trying to do so again will lead to an infinite
            // loop and a StackOverflowException.

            if(evaluationStack.contains(symbolExpr)) {
                return batch.addEmptyColumn(rootFormClass);
            }

            evaluationStack.push(symbolExpr);

            try {

                if (symbolExpr.getName().equals(ColumnModel.ID_SYMBOL)) {
                    return batch.addResourceIdColumn(rootFormClass);

                } else if (symbolExpr.getName().equals(ColumnModel.CLASS_SYMBOL)) {
                    return batch.addConstantColumn(rootFormClass, rootFormClass.getId().asString());
                }

                Collection<NodeMatch> nodes = resolver.resolveSymbol(symbolExpr);
                LOGGER.finer(symbolExpr + " matched to " + nodes);
                return addColumn(nodes);

            } finally {
                evaluationStack.pop();
            }
        }

        @Override
        public Slot<ColumnView> visitGroup(GroupExpr group) {
            return group.getExpr().accept(this);
        }

        @Override
        public Slot<ColumnView> visitCompoundExpr(CompoundExpr compoundExpr) {
            return addColumn(resolver.resolveCompoundExpr(compoundExpr));
        }

        @Override
        public Slot<ColumnView> visitFunctionCall(final FunctionCallNode call) {
            if(call.getFunction() instanceof ColumnFunction) {
                if(call.getArguments().isEmpty()) {
                    return createNullaryFunctionCall(call);
                } else {
                    return createFunctionCall(call);
                }
            } else {
                return batch.addExpression(rootFormClass, call);
            }
        }

        private Slot<ColumnView> createNullaryFunctionCall(final FunctionCallNode call) {
            assert call.getArguments().isEmpty();

            final Slot<Integer> rowCount = batch.addRowCount(rootFormClass);

            return new Slot<ColumnView>() {
                @Override
                public ColumnView get() {
                    return ((ColumnFunction) call.getFunction()).columnApply(rowCount.get(),
                            Collections.<ColumnView>emptyList());
                }
            };

        }

        private Slot<ColumnView> createFunctionCall(final FunctionCallNode call) {
            final List<Slot<ColumnView>> argumentSlots = Lists.newArrayList();
            for(ExprNode argument : call.getArguments()) {
                argumentSlots.add(argument.accept(this));
            }
            return new Slot<ColumnView>() {
                @Override
                public ColumnView get() {
                    List<ColumnView> arguments = Lists.newArrayList();
                    for (Slot<ColumnView> argument : argumentSlots) {
                        ColumnView view = argument.get();
                        if(view == null) {
                            throw new IllegalStateException();
                        }
                        arguments.add(view);
                    }
                    return ((ColumnFunction) call.getFunction()).columnApply(arguments.get(0).numRows(), arguments);
                }
            };
        }

        private Slot<ColumnView> addColumn(Collection<NodeMatch> nodes) {
            // Recursively expand any calculated fields
            List<Slot<ColumnView>> expandedNodes = Lists.newArrayList();
            for (NodeMatch node : nodes) {
                switch (node.getType()) {
                    case ID:
                        expandedNodes.add(batch.addColumn(node));
                        break;
                    case CLASS:
                        throw new UnsupportedOperationException();
                    case FIELD:
                        if (node.isCalculated()) {
                            expandedNodes.add(expandCalculatedField(node));
                        } else {
                            expandedNodes.add(batch.addColumn(node));
                        }
                        break;
                }
            }
            if(expandedNodes.isEmpty()) {
                return batch.addEmptyColumn(rootFormClass);
            } else if(expandedNodes.size() == 1) {
                return expandedNodes.get(0);
            } else {
                return new ColumnCombiner(expandedNodes);
            }
        }

        private Slot<ColumnView> expandCalculatedField(NodeMatch node) {
            try {
                return evaluateExpression(node.getCalculation());
            } catch (ExprException e) {
                LOGGER.log(Level.WARNING, "Exception in calculated field " +
                        node.getFormClass().getId() + "." + node.getExpr() + " = " +
                        node.getCalculation() + ": " + e.getMessage(), e);
            
                return batch.addEmptyColumn(node.getFormClass());
            }
        }
    }
}
