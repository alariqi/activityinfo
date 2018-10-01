package org.activityinfo.ui.client.reports.pivot.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.reports.formSelection.view.FormSelectionView;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.client.reports.pivot.state.DesignPanelState;
import org.activityinfo.ui.client.reports.pivot.state.PivotUpdater;
import org.activityinfo.ui.client.reports.pivot.viewModel.PivotPageViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class PivotView {

    public static VTree render(PivotPageViewModel viewModel, PivotUpdater updater) {

        return new PageBuilder()
            .heading(I18N.CONSTANTS.reports())
            .breadcrumbs(viewModel.getBreadcrumbs())
            .body(renderBody(viewModel, updater))
            .build();

    }

    private static VTree renderBody(PivotPageViewModel viewModel, PivotUpdater updater) {

        return H.div("pivot",
                renderPanel(viewModel, updater),
                renderTable(viewModel));

    }

    private static VTree renderTable(PivotPageViewModel viewModel) {
        return H.div();
    }

    private static VTree renderPanel(PivotPageViewModel viewModel, PivotUpdater updater) {

        return new ReactiveComponent(viewModel.getPanelState().transform(state -> {
            if(state == DesignPanelState.FORM_SELECTION) {
                return renderFormSelection(viewModel, updater);
            } else {
                return H.div();
            }
        }));
    }

    private static VTree renderFormSelection(PivotPageViewModel viewModel, PivotUpdater updater) {

        return FormSelectionView.render(viewModel.getFormSelection(), updater);


    }

    private static VTree renderAvailableFields(Observable<FormSelectionViewModel> formSelection) {
        return null;
    }
}
