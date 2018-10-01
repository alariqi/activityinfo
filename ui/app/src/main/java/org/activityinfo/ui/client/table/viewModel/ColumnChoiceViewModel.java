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
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ColumnChoiceViewModel implements FieldChoiceViewModel {

    private static final Logger LOGGER = Logger.getLogger(ColumnChoiceViewModel.class.getName());

    private final String GROUP_ID = "columns";

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
    public Observable<FormColumns> getFormSelection() {
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
    public Observable<ReportListViewModel> getReportElements() {
        Observable<EffectiveTableModel> table = tableModel.getEffectiveTable();
        Observable<FieldChoiceState> state = tableModel.getModel().transform(m -> m.getColumnOptions());

        return Observable.transform(table, state, (t, s) -> {
           List<ReportElement> items = new ArrayList<>();
            for (EffectiveTableColumn column : t.getColumns()) {
                items.add(reportElement(t.getFormTree(), column));
            }

            int dropIndex = -1;
            if(s.isDragging() && s.getDropTarget().isPresent()) {
                dropIndex = s.getDropTarget().get().getIndex();
            }

            LOGGER.info("ColumnChoiceViewModel: dropIndex = " + dropIndex);

            return new ReportListViewModel(new ReportElementGroup(GROUP_ID,"", items, dropIndex));
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
