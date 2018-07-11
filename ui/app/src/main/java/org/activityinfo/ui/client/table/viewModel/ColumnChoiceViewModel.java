package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.analysis.ParsedFormula;
import org.activityinfo.analysis.table.EffectiveTableColumn;
import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.analysis.viewModel.FieldTypes;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.fields.state.DesignMode;
import org.activityinfo.ui.client.fields.state.FieldChoiceState;
import org.activityinfo.ui.client.fields.viewModel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<String> getReportElementHeader() {
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
        Observable<EffectiveTableModel> table = tableModel.getEffectiveTable();
        Observable<FieldChoiceState> state = tableModel.getModel().transform(s -> s.getColumnOptions());

        Observable<FieldListViewModel> listView = table.transform(t -> {
            return new FieldListBuilder(t.getIncludedFormulas()).build(t.getFormTree());
        });

        return Observable.transform(listView, state, (list, s) -> {
            return new FieldListViewModel(list, s.getDraggingFieldId());
        });
    }

    @Override
    public Observable<ReportElementListView> getReportElements() {
        return tableModel.getEffectiveTable().transform(table -> {
           List<ReportElement> items = new ArrayList<>();
            for (EffectiveTableColumn column : table.getColumns()) {
                items.add(reportElement(table.getFormTree(), column));
            }
            return new ReportElementListView(new ReportElementGroup("", items));
        });
    }

    private ReportElement reportElement(FormTree formTree, EffectiveTableColumn column) {
        ParsedFormula formula = column.getFormula();
        if(!formula.isValid()) {
            return new ReportElement(column.getId(),"ERROR", "ERROR", column.getLabel());
        }

        String form = I18N.CONSTANTS.thisForm();

        return new ReportElement(column.getId(), form, FieldTypes.localizedFieldType(formula.getResultType()), column.getLabel());
    }

}
