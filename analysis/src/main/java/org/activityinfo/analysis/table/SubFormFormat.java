package org.activityinfo.analysis.table;

import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.resource.ResourceId;

public class SubFormFormat extends SimpleColumnFormat<Integer> {

    private final ResourceId subFormId;

    protected SubFormFormat(String id, FormulaNode formula, ResourceId subFormId) {
        super(id, formula);
        this.subFormId = subFormId;
    }

    public ResourceId getSubFormId() {
        return subFormId;
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
