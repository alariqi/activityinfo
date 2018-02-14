package org.activityinfo.analysis.table;

import org.activityinfo.analysis.FieldReference;
import org.activityinfo.analysis.ParsedFormula;
import org.activityinfo.model.analysis.TableColumn;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.type.FieldType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.store.query.shared.NodeMatch;

import java.util.List;
import java.util.Optional;

public class EffectiveTableColumn {

    public static final int DEFAULT_COLUMN_WIDTH = 100;
    private TableColumn model;
    private String label;
    private ParsedFormula formula;
    private ColumnFormat format;
    private int width;

    public EffectiveTableColumn(FormTree formTree, TableColumn model) {
        this.model = model;
        this.formula = new ParsedFormula(formTree, model.getFormula());
        if(model.getLabel().isPresent()) {
            this.label = model.getLabel().get();
        } else {
            this.label = formula.getLabel();
        }
        format = ColumnFormatFactory.create(model.getId(), formula);
        width = model.getWidth().or(DEFAULT_COLUMN_WIDTH);
    }

    public ParsedFormula getFormula() {
        return formula;
    }

    public String getFormulaString() {
        return formula.getFormula();
    }

    public FieldType getType() {
        if(formula.isValid()) {
            return formula.getResultType();
        } else {
            return TextType.SIMPLE;
        }
    }

    public String getId() {
        return model.getId();
    }

    public String getLabel() {
        return label;
    }

    public Optional<FormTree.Node> getField() {
        if (formula.isValid()) {
            if (!formula.isCalculated()) {
                if (formula.isSimpleReference()) {
                    FieldReference reference = formula.getReferences().get(0);
                    NodeMatch match = reference.getMatch();
                    if (match.getType() == NodeMatch.Type.FIELD) {
                        return Optional.of(match.getFieldNode());
                    }
                }
            }
        }
        return Optional.empty();
    }

    public TableColumn getModel() {
        return model;
    }

    public List<ColumnModel> getQueryModel() {
        return format.getColumnModels();
    }

    public boolean isValid() {
        return formula.isValid();
    }

    public int getWidth() {
        return width;
    }

    public <T> T accept(TableColumnVisitor<T> visitor) {
        return format.accept(this, visitor);
    }

    @Override
    public String toString() {
        return "EffectiveTableColumn{" + label + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EffectiveTableColumn that = (EffectiveTableColumn) o;

        if (!model.equals(that.model)) {
            return false;
        }
        if (!label.equals(that.label)) {
            return false;
        }
        if (!formula.equals(that.formula)) {
            return false;
        }
        return format.equals(that.format);

    }

    @Override
    public int hashCode() {
        int result = model.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + formula.hashCode();
        result = 31 * result + format.hashCode();
        return result;
    }
}
