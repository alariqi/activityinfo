package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.fields.state.DesignMode;

import java.util.Optional;

/**
 * A common interface to the different types of report and table models
 * that expose some kind of field selection.
 */
public interface FieldChoiceViewModel {

    String getPanelTitle();

    boolean isFormSelectionEnabled();

    SidePanel.HideMode getPanelHideMode();

    /**
     * @return an optional heading for the selected fields column.
     */
    Optional<String> getReportElementHeader();

    Observable<DesignMode> getMode();

    Observable<FormSelectionViewModel> getFormSelection();

    Observable<FieldListViewModel> getAvailableFields();

    Observable<ReportListViewModel> getReportElements();

    Observable<Boolean> isExpanded();
}
