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
package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.analysis.table.EffectiveTableColumn;
import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.model.database.Operation;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.form.RecordHistory;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.formula.ConstantNode;
import org.activityinfo.model.formula.Formulas;
import org.activityinfo.model.formula.SymbolNode;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.table.model.TableModel;

import java.util.*;
import java.util.logging.Logger;

/**
 * Model's the user's selection of columns
 */
public class TableViewModel {
    private static final Logger LOGGER = Logger.getLogger(TableViewModel.class.getName());

    public static final String ID_COLUMN_ID = "$$id";
    private static final String EDIT_COLUMN_ID = "$$edit";

    private final FormSource formStore;
    private ResourceId formId;
    private FormTree formTree;
    private boolean visible;
    private TableModel tableModel;
    private EffectiveTableModel effectiveTable;
    private Observable<ColumnSet> columnSet;
    private final Optional<String> parentRecordId;
    private final int depth;
    private final List<FormClass> subForms;

    private Observable<Map<String, Integer>> recordMap;

    public TableViewModel(final FormSource formStore, FormTree formTree, TableModel tableModel, boolean visible, int depth, Optional<String> parentRecordId) {
        this.formId = formTree.getRootFormId();
        this.formStore = formStore;
        this.formTree = formTree;
        this.tableModel = tableModel;
        this.visible = visible;
        this.depth = depth;
        this.effectiveTable = new EffectiveTableModel(formTree, tableModel.getAnalysisModel());
        this.parentRecordId = parentRecordId;
        this.columnSet = queryColumns(formStore, effectiveTable);
        this.recordMap = this.columnSet.transform(this::buildRecordMap);
        this.subForms = new ArrayList<>();
        for (FormTree.Node node : formTree.getRootFields()) {
            if(node.isVisibleSubForm()) {
                SubFormReferenceType type = (SubFormReferenceType) node.getType();
                subForms.add(formTree.getFormClass(type.getClassId()));
            }
        }
    }

    public boolean isRecordPanelExpanded() {
        return tableModel.isRecordPanelExpanded();
    }

    public Optional<String> getParentRecordId() {
        return parentRecordId;
    }

    private Observable<ColumnSet> queryColumns(FormSource formStore, EffectiveTableModel table) {
        QueryModel queryModel = new QueryModel(table.getFormId());
        queryModel.selectResourceId().as(ID_COLUMN_ID);
        queryModel.selectExpr(editRule()).as(EDIT_COLUMN_ID);
        parentRecordId.ifPresent(parentId -> {
            queryModel.setFilter(Formulas.equals(new SymbolNode("@parent"), new ConstantNode(parentId)));
        });
        for (EffectiveTableColumn column : table.getColumns()) {
            queryModel.addColumns(column.getQueryModel());
        }
        return formStore.query(queryModel);
    }

    private String editRule() {
        FormMetadata rootMetadata = formTree.getRootMetadata();
        if(rootMetadata == null) {
            return "FALSE";
        }
        String formula = rootMetadata.getPermissions().getFilter(Operation.EDIT_RECORD);
        if(formula == null) {
            return "TRUE";
        }
        return formula;
    }

    private Map<String, Integer> buildRecordMap(ColumnSet columnSet) {
        ColumnView columnView = columnSet.getColumns().get(ID_COLUMN_ID);
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < columnView.numRows(); i++) {
            String recordId = columnView.getString(i);
            map.put(recordId, i);
        }
        return map;
    }

    /**
     *
     * @return the depth of this table in the table tree, where the root form is zero, the first level
     * subform is one, etc.
     */
    public int getDepth() {
        return depth;
    }

    public Observable<java.util.Optional<RecordRef>> getSelectedRecordRef() {
        // Don't actually expose the internal selection state ...
        // the *effective* selection is a product of our model state and the record status (deleted or not)
        // and filtering

        Optional<RecordRef> ref = tableModel.getSelected();

        return recordMap.transform(map -> {
           if(!ref.isPresent()) {
               return java.util.Optional.empty();
           }
           if(!map.containsKey(ref.get().getRecordId().asString())) {
               return java.util.Optional.empty();
           }
           return ref;
        });
    }

    public Observable<Boolean> hasSelection() {
        return getSelectedRecordRef().transform(r -> r.isPresent());
    }

    public Observable<Optional<RecordHistory>> getSelectedRecordHistory() {
        return getSelectedRecordRef().join(ref -> {
            if (ref.isPresent()) {
                return formStore.getFormRecordHistory(ref.get()).transform(h -> Optional.of(h));
            } else {
                return Observable.just(Optional.empty());
            }
        });
    }

    public Observable<Optional<RecordTree>> getSelectedRecordTree() {
        return getSelectedRecordRef().join(optionalRef -> {
            if(optionalRef.isPresent()) {
                return formStore.getRecordTree(optionalRef.get()).transform(maybe -> maybe.getIfVisible().toJavaUtil());
            } else {
                return Observable.just(Optional.empty());
            }
        });
    }

    public EffectiveTableModel getEffectiveTable() {
        return effectiveTable;
    }

    public Observable<Maybe<UserDatabaseMeta>> getDatabase() {
        return formStore.getDatabase(effectiveTable.getDatabaseId());
    }

    public Optional<FormInputModel> getInputModel() {
        return tableModel.getInputModel();
    }

    public Observable<ColumnSet> getColumnSet() {
        return columnSet;
    }

    public ResourceId getFormId() {
        return formId;
    }

    public FormTree getFormTree() {
        return formTree;
    }

    public boolean isVisible() {
        return visible;
    }

    public List<FormClass> getSubForms() {
        return subForms;
    }

    public TableModel.EditMode getEditMode() {
        return tableModel.getEditMode();
    }
}
