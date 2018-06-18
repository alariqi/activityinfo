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

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.activityinfo.analysis.ParsedFormula;
import org.activityinfo.model.analysis.ImmutableTableColumn;
import org.activityinfo.model.analysis.ImmutableTableModel;
import org.activityinfo.model.analysis.TableModel;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.form.RecordHistory;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.formula.CompoundExpr;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.formula.SymbolNode;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.store.query.shared.FormSource;

import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * Model's the user's selection of columns
 */
public class TableViewModel {

    private static final Logger LOGGER = Logger.getLogger(TableViewModel.class.getName());

    private final FormSource formStore;
    private ResourceId formId;
    private Observable<FormTree> formTree;
    private Observable<TableModel> tableModel;
    private Observable<EffectiveTableModel> effectiveTable;
    private Observable<ColumnSet> columnSet;

    private StatefulValue<Optional<RecordRef>> selectedRecordRef = new StatefulValue<>(Optional.absent());
    private final Observable<Optional<SelectionViewModel>> selectionViewModel;

    public TableViewModel(final FormSource formStore, Observable<TableModel> tableModel) {
        this.formId = tableModel.get().getFormId();
        this.formStore = formStore;
        this.formTree = formStore.getFormTree(formId);
        this.tableModel = tableModel;
        this.effectiveTable = computeEffectiveTableModel(this.tableModel);

        this.selectionViewModel = SelectionViewModel.compute(formStore, selectedRecordRef);
        this.columnSet = this.effectiveTable.join(table -> table.getColumnSet());
    }

    public Observable<EffectiveTableModel> computeEffectiveTableModel(Observable<TableModel> tableModel) {
        return tableModel.join(tm -> {
            return formTree.transform(tree -> new EffectiveTableModel(formStore, tree, tm));
        });
    }

    public Observable<TableModel> getTableModel() {
        return tableModel;
    }

    public Observable<Optional<RecordRef>> getSelectedRecordRef() {
        // Don't actually expose the internal selection state ...
        // the *effective* selection is a product of our model state and the record status (deleted or not)
        return getSelectionViewModel().transform(record -> {
            if(record.isPresent()) {
                return Optional.of(record.get().getRef());
            } else {
                return Optional.absent();
            }
        });
    }

    public Observable<Optional<RecordHistory>> getSelectedRecordHistory() {
        return getSelectedRecordRef().join(ref -> {
            if (ref.isPresent()) {
                return formStore.getFormRecordHistory(ref.get()).transform(h -> Optional.of(h));
            } else {
                return Observable.just(Optional.absent());
            }
        });
    }

    public Observable<Optional<SelectionViewModel>> getSelectionViewModel() {
        return selectionViewModel;
    }

    public Observable<Optional<RecordTree>> getSelectedRecordTree() {
        return getSelectionViewModel().join(new Function<Optional<SelectionViewModel>, Observable<Optional<RecordTree>>>() {
            @Override
            public Observable<Optional<RecordTree>> apply(@Nullable Optional<SelectionViewModel> selection) {
                if(selection.isPresent()) {
                    return formStore.getRecordTree(selection.get().getRef()).transform(tree -> tree.getIfVisible());
                } else {
                    return Observable.just(Optional.absent());
                }
            }
        });
    }

    public Observable<EffectiveTableModel> getEffectiveTable() {
        return effectiveTable;
    }

    public Observable<Maybe<UserDatabaseMeta>> getDatabase() {
        return effectiveTable.join(t -> formStore.getDatabase(t.getDatabaseId()));
    }

    public Observable<ApiViewModel> getApiViewModel() {
        return effectiveTable.transform(ApiViewModel::new);
    }

    public Observable<ColumnSet> getColumnSet() {
        return columnSet;
    }

    private Observable<EffectiveTableModel> getEffectiveSubTable(final ResourceId subFormId) {
        final TableModel subModel = ImmutableTableModel.builder()
                .formId(subFormId)
                .build();

        return formTree
                .transform(tree -> tree.subTree(subFormId))
                .transform(subTree -> new EffectiveTableModel(formStore, subTree, subModel, Optional.of(getSelectedRecordRef())));
    }

    public ResourceId getFormId() {
        return formId;
    }

    public Observable<FormTree> getFormTree() {
        return formTree;
    }

    public void select(RecordRef ref) {
        selectedRecordRef.updateIfNotEqual(Optional.of(ref));
    }


    public Observable<ExportViewModel> computeExportModel(
            Observable<ResourceId> selectedForm,
            Observable<ExportScope> columnScope) {
        return computeExportModel(selectedForm, columnScope, Observable.just(ExportScope.ALL));
    }

    public Observable<ExportViewModel> computeExportModel(
            Observable<ResourceId> selectedForm,
            Observable<ExportScope> columnScope,
            Observable<ExportScope> rowScope) {

        Observable<EffectiveTableModel> parentFormModel = getEffectiveTable();
        Observable<Optional<EffectiveTableModel>> subFormModel = selectedForm.join(formId -> {
            if(formId.equals(tableModel.get().getFormId())) {
                // Parent form has been selected, no sub form model
                return Observable.just(Optional.absent());
            } else {
                return getEffectiveSubTable(formId).transform(Optional::of);
            }
        });

        Observable<TableModel> exportTableModel = Observable.transform(parentFormModel, subFormModel, columnScope, rowScope, (parent, sub, columns, rows) -> {
            ImmutableTableModel.Builder model = ImmutableTableModel.builder();
            if(sub.isPresent()) {
                model.formId(sub.get().getFormId());
                if(columns == ExportScope.SELECTED ) {
                    for (EffectiveTableColumn tableColumn : parent.getColumns()) {
                        model.addColumns(ImmutableTableColumn.builder()
                                .label(tableColumn.getLabel())
                                .formula(parentFormula(tableColumn.getFormula()))
                                .build());

                    }
                    for (EffectiveTableColumn tableColumn : sub.get().getColumns()) {
                        model.addColumns(ImmutableTableColumn.builder()
                                .label(tableColumn.getLabel())
                                .formula(tableColumn.getFormulaString())
                                .build());
                    }
                }
                if (rows == ExportScope.SELECTED) {
                    model.filter(parent.getFilter());
                }
            } else {
                model.formId(parent.getFormId());
                if(columns == ExportScope.SELECTED) {
                    for (EffectiveTableColumn tableColumn : parent.getColumns()) {
                        model.addColumns(ImmutableTableColumn.builder()
                                .label(tableColumn.getLabel())
                                .formula(tableColumn.getFormulaString())
                                .build());

                    }
                }
                if (rows == ExportScope.SELECTED) {
                    model.filter(parent.getFilter());
                }
            }
            return model.build();
        });
        Observable<Boolean> colLimitExceed = computeEffectiveTableModel(exportTableModel).transform(ExportViewModel::columnLimitExceeded);
        return Observable.transform(exportTableModel, colLimitExceed, ExportViewModel::new);
    }

    private String parentFormula(ParsedFormula formula) {

        if(!formula.isValid()) {
            return formula.getFormula();
        }

        SymbolNode parentSymbol = new SymbolNode(ColumnModel.PARENT_SYMBOL);
        FormulaNode transformed = formula.getRootNode().transform(node -> {
           if(node instanceof SymbolNode) {
               // A -> parent.A
               return new CompoundExpr(parentSymbol, (SymbolNode) node);
           } else {
               return node;
           }
        });
        return transformed.asExpression();
    }
}
