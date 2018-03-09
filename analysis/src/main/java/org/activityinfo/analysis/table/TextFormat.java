package org.activityinfo.analysis.table;

import org.activityinfo.model.formula.FormulaNode;

/**
 * Simple column format for a field that can be represented as single
 * text column.
 */
public class TextFormat extends SimpleColumnFormat<String> {

    protected TextFormat(String id, FormulaNode formula) {
        super(id, formula);
    }

    @Override
    public ColumnRenderer<String> createRenderer() {
        return new StringRenderer(getId());
    }


    @Override
    public <T> T accept(EffectiveTableColumn columnModel, TableColumnVisitor<T> visitor) {
        return visitor.visitTextColumn(columnModel, this);
    }

}
