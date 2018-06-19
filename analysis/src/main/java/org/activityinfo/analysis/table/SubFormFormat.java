package org.activityinfo.analysis.table;

import org.activityinfo.model.formula.FormulaNode;

public class SubFormFormat extends SimpleColumnFormat<Integer> {

    protected SubFormFormat(String id, FormulaNode formula) {
        super(id, formula);
    }

    @Override
    public ColumnRenderer<Integer> createRenderer() {
       return new CountRenderer(getId());
    }

    @Override
    public <T> T accept(EffectiveTableColumn columnModel, TableColumnVisitor<T> visitor) {
        return visitor.visitSubFormColumn(columnModel, this);
    }
}
