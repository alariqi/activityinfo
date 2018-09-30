package org.activityinfo.ui.client.reports.pivot.viewModel;

import org.activityinfo.analysis.pivot.viewModel.PivotViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.analysis.pivot.PivotModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.reports.ReportListPlace;
import org.activityinfo.ui.client.reports.formSelection.state.FormPath;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.client.reports.pivot.PivotPlace;
import org.activityinfo.ui.client.reports.pivot.state.DesignPanelState;
import org.activityinfo.ui.client.reports.pivot.state.PivotState;
import org.activityinfo.ui.client.store.FormStore;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PivotPageViewModel {

    public static final Breadcrumb REPORTS_BREADCRUMB = new Breadcrumb(I18N.CONSTANTS.reports(), new ReportListPlace());

    private final Observable<PivotState> state;
    private final Observable<FormSelectionViewModel> formSelection;
    private final Observable<PivotModel> model;
    private final PivotViewModel pivotViewModel;

    public PivotPageViewModel(FormStore formStore, Observable<PivotState> state) {
        this.state = state;
        this.model = state.transform(s -> s.getModel());
        this.pivotViewModel = new PivotViewModel(model, formStore);

        Observable<Set<ResourceId>> selectedForms = state.transform(s -> s.getModel().getForms()).cache();
        Observable<FormPath> formSelectionPath = state.transform(s -> s.getFormSelection()).cache();

        this.formSelection = FormSelectionViewModel.compute(formStore, selectedForms, formSelectionPath);
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