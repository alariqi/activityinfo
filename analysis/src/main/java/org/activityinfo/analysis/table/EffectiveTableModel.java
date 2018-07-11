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

import org.activityinfo.model.analysis.ImmutableTableColumn;
import org.activityinfo.model.analysis.TableAnalysisModel;
import org.activityinfo.model.analysis.TableColumn;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.LookupKey;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.formula.ConstantNode;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.formula.Formulas;
import org.activityinfo.model.formula.SymbolNode;
import org.activityinfo.model.query.*;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.*;
import org.activityinfo.model.type.barcode.BarcodeType;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.expr.CalculatedFieldType;
import org.activityinfo.model.type.geo.GeoPointType;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.model.type.time.EpiWeekType;
import org.activityinfo.model.type.time.FortnightType;
import org.activityinfo.model.type.time.LocalDateType;
import org.activityinfo.model.type.time.MonthType;
import org.activityinfo.observable.Observable;
import org.activityinfo.store.query.shared.FormSource;

import java.util.*;

/**
 * The effective description of a table
 */
public class EffectiveTableModel {


    private FormTree formTree;
    private TableAnalysisModel tableModel;
    private List<EffectiveTableColumn> columns;

    public EffectiveTableModel(FormTree formTree, TableAnalysisModel tableModel) {
        this.formTree = formTree;
        this.tableModel = tableModel;
        this.columns = new ArrayList<>();

        if(formTree.getRootState() == FormTree.State.VALID) {
            if (tableModel.getColumns().isEmpty()) {
                addDefaultColumns(formTree);
            } else {
                for (TableColumn tableColumn : tableModel.getColumns()) {
                    columns.add(new EffectiveTableColumn(formTree, tableColumn));
                }
            }
        }
    }

    private void addDefaultColumns(FormTree formTree) {
        for (FormTree.Node node : formTree.getRootFields()) {
            if(node.getField().isVisible()) {
                if (isSimple(node.getType())) {
                    columns.add(new EffectiveTableColumn(formTree, defaultColumnModel(node)));

                } else if (node.getType() instanceof ReferenceType && !node.isParentReference()) {
                    addKeyColumns(columns, node);

                } else if (node.isVisibleSubForm()) {
                    columns.add(new EffectiveTableColumn(formTree, defaultColumnModel(node)));
                }
            }
        }
    }


    public String getTitle() {
        return formTree.getRootFormClass().getLabel();
    }

    private boolean isSimple(FieldType type) {
        return type instanceof TextType ||
               type instanceof NarrativeType ||
               type instanceof SerialNumberType ||
               type instanceof QuantityType ||
               type instanceof BarcodeType ||
               type instanceof EnumType ||
               type instanceof GeoPointType ||
               type instanceof LocalDateType ||
               type instanceof EpiWeekType ||
               type instanceof FortnightType ||
               type instanceof MonthType ||
               type instanceof CalculatedFieldType;
    }

    private ImmutableTableColumn defaultColumnModel(FormTree.Node node) {
        return defaultColumnModel(node.getPath().toExpr());
    }

    private ImmutableTableColumn defaultColumnModel(FormulaNode formulaNode) {
        String formulaString = formulaNode.asExpression();

        // We need stable ids for our default columns, otherwise
        // the views will get confused and refresh unnecessarily
        String id = formulaString.replace('.', 'd');

        return ImmutableTableColumn.builder()
                .id(id)
                .formula(formulaString)
                .build();
    }


    private void addKeyColumns(List<EffectiveTableColumn> columns, FormTree.Node node) {

        LookupKeySet lookupKeySet = new LookupKeySet(formTree, node.getField());
        Map<LookupKey, FormulaNode> formulas = lookupKeySet.getKeyFormulas(node.getFieldId());

        for (LookupKey lookupKey : lookupKeySet.getLookupKeys()) {

            ImmutableTableColumn tableColumn = ImmutableTableColumn.builder()
                .id(node.getFieldId().asString() + "_k" + lookupKey.getKeyIndex())
                .formula(formulas.get(lookupKey).toString())
                .label(lookupKey.getKeyLabel())
                .build();

            columns.add(new EffectiveTableColumn(formTree, tableColumn));
        }
    }

    public ResourceId getFormId() {
        return formTree.getRootFormId();
    }

    private QueryModel buildQuery(List<EffectiveTableColumn> columns) {
        QueryModel queryModel = new QueryModel(formTree.getRootFormId());
        if(tableModel.getFilter().isPresent()) {
            queryModel.setFilter(tableModel.getFilter().get());
        }
        for (EffectiveTableColumn column : columns) {
            queryModel.addColumns(column.getQueryModel());
        }
        return queryModel;
    }

    private QueryModel buildQuery(List<EffectiveTableColumn> columns, RecordRef recordRef) {
        QueryModel queryModel = buildQuery(columns);
        queryModel.setFilter(Formulas.equals(new SymbolNode("@parent"), new ConstantNode(recordRef.getRecordId().asString())));

        return queryModel;
    }

    private ColumnSet emptyColumnSet(List<EffectiveTableColumn> columns) {

        Map<String, ColumnView> columnMap = new HashMap<>();
        for (EffectiveTableColumn column : columns) {
            for (ColumnModel columnModel : column.getQueryModel()) {
                columnMap.put(columnModel.getId(), new EmptyColumnView(ColumnType.STRING, 0));
            }
        }

        return new ColumnSet(0, columnMap);
    }

    public FormTree getFormTree() {
        return formTree;
    }

    public ResourceId getDatabaseId() {
        return formTree.getRootFormClass().getDatabaseId();
    }

    public List<EffectiveTableColumn> getColumns() {
        return columns;
    }

    public Observable<ColumnSet> queryColumns(FormSource formSource) {
        return formSource.query(buildQuery(columns));
    }

    /**
     * @return the set of formulas which are used as columns in this table
     */
    public Set<String> getIncludedFormulas() {
        Set<String> includedFormulas = new HashSet<>();
        for (EffectiveTableColumn column : columns) {
            includedFormulas.add(column.getFormulaString());
        }
        return includedFormulas;
    }
}
