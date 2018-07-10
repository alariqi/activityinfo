package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.analysis.ParsedFormula;
import org.activityinfo.analysis.table.EffectiveTableColumn;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.fields.model.DesignMode;
import org.activityinfo.ui.client.fields.viewModel.*;

import java.util.*;

public class ColumnChoiceViewModel implements FieldChoiceViewModel {

    private final TableViewModel tableModel;

    public ColumnChoiceViewModel(TableViewModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public String getPanelTitle() {
        return I18N.CONSTANTS.chooseColumns();
    }

    @Override
    public boolean isFormSelectionEnabled() {
        return false;
    }

    @Override
    public SidePanel.HideMode getPanelHideMode() {
        return SidePanel.HideMode.CLOSE;
    }

    @Override
    public Optional<String> getSelectedFieldsHeading() {
        return Optional.of(I18N.CONSTANTS.selectedColumns());
    }

    @Override
    public Observable<Boolean> isExpanded() {
        return tableModel.isColumnOptionsVisible();
    }

    @Override
    public Observable<DesignMode> getMode() {
        return Observable.just(DesignMode.NORMAL);
    }

    @Override
    public Observable<FormSelectionViewModel> getFormSelection() {
        return Observable.loading();
    }

    @Override
    public Observable<FieldListViewModel> getAvailableFields() {
        return tableModel.getEffectiveTable().transform(table -> {

            Set<String> includedFormulas = new HashSet<>();
            for (EffectiveTableColumn column : table.getColumns()) {
                includedFormulas.add(column.getFormulaString());
            }

            return new FieldListBuilder(includedFormulas).build(table.getFormTree());
        });
    }

    @Override
    public Observable<FieldListViewModel> getSelectedFields() {
        return tableModel.getEffectiveTable().transform(table -> {
           List<FieldListItem> items = new ArrayList<>();
            for (EffectiveTableColumn column : table.getColumns()) {
                items.add(fieldItem(table.getFormTree(), column));
            }
            return new FieldListViewModel(items);
        });
    }

    private FieldListItem fieldItem(FormTree formTree, EffectiveTableColumn column) {
        ParsedFormula formula = column.getFormula();
        if(!formula.isValid()) {
            return new FieldListItem("ERROR", "ERROR", column.getLabel());
        }

        String form = I18N.CONSTANTS.thisForm();

        return new FieldListItem(form, formula.getResultType(), column.getLabel());
    }

}
