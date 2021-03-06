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

import org.activityinfo.model.formula.CompoundExpr;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.formula.SymbolNode;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;

import java.util.ArrayList;
import java.util.List;

/**
 * Column format for an enum that can take multiple values.
 */
public class MultiEnumFormat implements ColumnFormat {

    private final String columnId;
    private final FormulaNode formula;
    private final EnumType enumType;

    private final int numItems;
    private final String[] labels;

    public MultiEnumFormat(String columnId, FormulaNode formula, EnumType enumType) {
        this.columnId = columnId;
        this.formula = formula;
        this.enumType = enumType;
        this.numItems = enumType.getValues().size();
        this.labels = new String[numItems];
        for (int i = 0; i < numItems; i++) {
            labels[i] = enumType.getValues().get(i).getLabel();
        }
    }

    public EnumType getEnumType() {
        return enumType;
    }

    public ColumnRenderer<String> createRenderer() {
        return new Renderer();
    }

    @Override
    public List<ColumnModel> getColumnModels() {
        List<ColumnModel> columns = new ArrayList<>();
        for (EnumItem enumItem : enumType.getValues()) {

            ColumnModel model = new ColumnModel();
            model.setId(getItemId(enumItem));
            model.setFormula(new CompoundExpr(formula, new SymbolNode(enumItem.getId())));

            columns.add(model);
        }

        return columns;
    }

    private String getItemId(EnumItem enumItem) {
        return columnId + ":" + enumItem.getId();
    }

    @Override
    public <T> T accept(EffectiveTableColumn columnModel, TableColumnVisitor<T> visitor) {
        return visitor.visitMultiEnumColumn(columnModel, this);
    }

    private class Renderer implements ColumnRenderer<String> {

        private ColumnView[] views;

        private Renderer() {
        }

        @Override
        public String render(int rowIndex) {
            StringBuilder sb = new StringBuilder();
            boolean needsSep = false;
            for (int i = 0; i < numItems; i++) {
                if(views[i].getBoolean(rowIndex) == ColumnView.TRUE) {
                    if(needsSep) {
                        sb.append(", ");
                    }
                    sb.append(labels[i]);
                    needsSep = true;
                }
            }

            if(sb.length() > 0) {
                return sb.toString();
            } else {
                return null;
            }
        }

        @Override
        public void updateColumnSet(ColumnSet columnSet) {
            views = new ColumnView[numItems];
            for (int i = 0; i < numItems; i++) {
                EnumItem item = enumType.getValues().get(i);
                views[i] = columnSet.getColumnView(getItemId(item));
            }
        }
    }
}
