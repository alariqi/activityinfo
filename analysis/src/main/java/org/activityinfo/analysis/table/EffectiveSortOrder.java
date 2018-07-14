package org.activityinfo.analysis.table;

import org.activityinfo.model.formula.FormulaNode;

public class EffectiveSortOrder {
    private FormulaNode formula;
    private boolean ascending;

    public EffectiveSortOrder(FormulaNode formula, boolean ascending) {
        this.formula = formula;
        this.ascending = ascending;
    }

    public FormulaNode getFormula() {
        return formula;
    }

    public boolean isAscending() {
        return ascending;
    }
}
