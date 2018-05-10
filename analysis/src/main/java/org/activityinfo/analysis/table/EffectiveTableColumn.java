/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.analysis.table;

import org.activityinfo.analysis.ParsedFormula;
import org.activityinfo.model.analysis.TableColumn;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.type.FieldType;
import org.activityinfo.model.type.geo.GeoPointType;
import org.activityinfo.model.type.primitive.TextType;

import java.util.List;

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

    public int getExportedColumns() {
        if (formula.isValid() && formula.getResultType() instanceof GeoPointType) {
            return 2;
        } else {
            return 1;
        }
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
