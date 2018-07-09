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
import org.activityinfo.model.analysis.TableAnalysisModel;
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
import org.activityinfo.ui.client.table.TablePlace;
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
    private final ResourceId formId;
    private final int slideIndex;
    private final boolean subForm;
    private final Observable<TableModel> tableModel;
    private final Observable<FormTree> formTree;
    private final Observable<Optional<RecordRef>> selection;
    private final Observable<TableAnalysisModel> analysisModel;
    private final Observable<EffectiveTableModel> effectiveTable;
    private final Observable<ColumnSet> columnSet;
    private final Observable<Map<String, Integer>> recordMap;
    private final Observable<String> parentRef;

    public TableViewModel(final FormSource formStore, SliderTree sliderTree, ResourceId formId, Observable<TableModel> tableModel, Observable<TablePlace> place) {
        this.formStore = formStore;
        this.formId = formId;
        this.slideIndex = sliderTree.getSlideIndex(formId);
        this.tableModel = tableModel;
        this.analysisModel = tableModel.transform(m -> m.getAnalysisModel()).cache();
        this.formTree = formStore.getFormTree(formId).transformIf(t -> t.toMaybe().getIfVisible());
        this.effectiveTable = Observable.transform(analysisModel, formTree, (a, t) -> new EffectiveTableModel(t, a));

        this.subForm = sliderTree.isSubForm(formId);
        if(subForm) {
            this.parentRef = place.transformIf(p -> {
                if(p.getFormId().equals(formId)) {
                    return com.google.common.base.Optional.fromJavaUtil(p.getParentId());
                } else {
                    return com.google.common.base.Optional.<String>absent();
                }
            });
        } else {
            this.parentRef = Observable.just("");
        }

        this.columnSet = Observable.join(effectiveTable, parentRef, (t, p) -> queryColumns(formStore, t, p));
        this.recordMap = this.columnSet.transform(this::buildRecordMap);

        this.selection = Observable.transform(recordMap, tableModel.transform(m -> m.getSelected()).cache(), (m, s) -> {
            if(s.isPresent() && m.containsKey(s.get().getRecordId().asString())) {
                return s;
            } else {
                return Optional.empty();
            }
        });
    }

    public int getSlideIndex() {
        return slideIndex;
    }

    public Observable<Boolean> isRecordPanelExpanded() {
        return tableModel.transform(m -> m.isRecordPanelExpanded()).cache();
    }

    public Observable<String> getParentRecordId() {
        return parentRef;
    }

    private Observable<ColumnSet> queryColumns(FormSource formStore, EffectiveTableModel table, String parentId) {
        QueryModel queryModel = new QueryModel(table.getFormId());
        queryModel.selectResourceId().as(ID_COLUMN_ID);
        queryModel.selectExpr(editRule(table.getFormTree())).as(EDIT_COLUMN_ID);
        if(!parentId.isEmpty()) {
            queryModel.setFilter(Formulas.equals(new SymbolNode("@parent"), new ConstantNode(parentId)));
        }
        for (EffectiveTableColumn column : table.getColumns()) {
            queryModel.addColumns(column.getQueryModel());
        }
        return formStore.query(queryModel);
    }

    private String editRule(FormTree formTree) {
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


    public Observable<java.util.Optional<RecordRef>> getSelectedRecordRef() {
        return selection;
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

    public Observable<EffectiveTableModel> getEffectiveTable() {
        return effectiveTable;
    }

    public Observable<Maybe<UserDatabaseMeta>> getDatabase() {
        return effectiveTable.join(t -> formStore.getDatabase(t.getDatabaseId()));
    }

    public Optional<FormInputModel> getInputModel() {
        return Optional.empty();
    }

    public Observable<ColumnSet> getColumnSet() {
        return columnSet;
    }

    public ResourceId getFormId() {
        return formId;
    }


    public Observable<FormTree> getFormTree() {
        return formTree;
    }

    public Observable<List<FormLink>> getParentRecords() {
        return formTree.transform(tree -> {
            if(tree.getRootFormClass().isSubForm()) {
                ResourceId parentFormId = tree.getRootFormClass().getParentFormId().get();
                FormClass parentForm = tree.getFormClass(parentFormId);
                if(parentForm != null) {
                    return Collections.singletonList(new FormLink(new TablePlace(parentFormId), parentForm.getLabel()));
                }
            }
            return Collections.emptyList();
        });
    }

    public Observable<List<FormLink>> getSubForms() {
        return Observable.transform(formTree, getSelectedRecordRef(), (tree, s) -> {
            List<FormLink> list = new ArrayList<>();
            if(s.isPresent()) {
                for (FormTree.Node node : tree.getRootFields()) {
                    if (node.isVisibleSubForm()) {
                        SubFormReferenceType type = (SubFormReferenceType) node.getType();
                        FormClass subForm = tree.getFormClass(type.getClassId());
                        list.add(new FormLink(new TablePlace(subForm.getId(), s.get()), subForm.getLabel()));
                    }
                }
            }
            return list;
        });
    }

}
