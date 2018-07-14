package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formula.FormulaNode;

public class ConditionViewModel {
    private final FormTree formTree;
    private final FormulaNode condition;

    public ConditionViewModel(FormTree formTree, FormulaNode condition) {
        this.formTree = formTree;
        this.condition = condition;
    }
}
