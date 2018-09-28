package org.activityinfo.ui.client.reports.pivot.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.reports.ReportListPlace;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionBuilder;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.client.reports.pivot.PivotPlace;
import org.activityinfo.ui.client.reports.pivot.state.DesignPanelState;
import org.activityinfo.ui.client.reports.pivot.state.PivotState;
import org.activityinfo.ui.client.store.FormStore;

import java.util.Arrays;
import java.util.List;

public class PivotPageViewModel {

    public static final Breadcrumb REPORTS_BREADCRUMB = new Breadcrumb(I18N.CONSTANTS.reports(), new ReportListPlace());

    private final Observable<PivotState> state;
    private final Observable<FormSelectionViewModel> formSelection;

    public PivotPageViewModel(FormStore formStore, Observable<PivotState> state) {
        this.state = state;
        this.formSelection = state.transform(s -> FormSelectionBuilder.compute(formStore, s.getFormSelection()));

    }

    public List<Breadcrumb> getBreadcrumbs() {
        return Arrays.asList(
                REPORTS_BREADCRUMB,
                new Breadcrumb(I18N.CONSTANTS.untitledReport(), new PivotPlace("foobar")));
    }

    public Observable<DesignPanelState> getPanelState() {
        return Observable.just(DesignPanelState.FORM_SELECTION);
    }

    public Observable<FormSelectionViewModel> getFormSelection() {
        return formSelection;
    }
}