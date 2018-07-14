package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.formula.Formulas;
import org.activityinfo.model.formula.functions.AndFunction;

import java.util.ArrayList;
import java.util.List;

public class FilterViewModel {

    private List<ConditionViewModel> conditionViews = new ArrayList<>();

    public FilterViewModel(EffectiveTableModel tableModel) {
        tableModel.getFilter().ifPresent(filter -> {

            List<FormulaNode> conditions = Formulas.findBinaryTree(filter.getRootNode(), AndFunction.INSTANCE);
            for (FormulaNode condition : conditions) {
                conditionViews.add(new ConditionViewModel(tableModel.getFormTree(), condition));
            }
        });
    }

    public boolean isActive() {
        return !conditionViews.isEmpty();
    }

    public List<ConditionViewModel> getConditionViews() {
        return conditionViews;
    }
}
