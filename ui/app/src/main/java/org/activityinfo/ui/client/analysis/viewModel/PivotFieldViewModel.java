package org.activityinfo.ui.client.analysis.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.fields.state.DesignMode;
import org.activityinfo.ui.client.fields.viewModel.FieldChoiceViewModel;
import org.activityinfo.ui.client.fields.viewModel.FieldListViewModel;
import org.activityinfo.ui.client.fields.viewModel.ReportListViewModel;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormColumns;

import java.util.Optional;

public class PivotFieldViewModel implements FieldChoiceViewModel {

    @Override
    public String getPanelTitle() {
        return I18N.CONSTANTS.reportDesign();
    }

    @Override
    public boolean isFormSelectionEnabled() {
        return true;
    }

    @Override
    public SidePanel.HideMode getPanelHideMode() {
        return SidePanel.HideMode.COLLAPSE;
    }

    @Override
    public Optional<String> getReportElementHeader() {
        return Optional.empty();
    }

    @Override
    public Observable<DesignMode> getMode() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<FormColumns> getFormSelection() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<FieldListViewModel> getAvailableFields() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<ReportListViewModel> getReportElements() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<Boolean> isExpanded() {
        throw new UnsupportedOperationException("TODO");
    }
}
