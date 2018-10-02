package org.activityinfo.ui.client.reports.pivot.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.reports.formSelection.view.FormSelectionView;
import org.activityinfo.ui.client.reports.pivot.state.DesignPanelState;
import org.activityinfo.ui.client.reports.pivot.state.PivotUpdater;
import org.activityinfo.ui.client.reports.pivot.viewModel.PivotPageViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class PivotView {

    public static VTree render(PivotPageViewModel viewModel, PivotUpdater updater) {

        return new PageBuilder()
            .heading(I18N.CONSTANTS.untitledReport())
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
        return H.div("pivot__result");
    }

    private static VTree renderPanel(PivotPageViewModel viewModel, PivotUpdater updater) {

        return new ReactiveComponent(viewModel.getPanelState().transform(state -> {
            if(state == DesignPanelState.FORM_SELECTION) {
                return renderFormSelection(viewModel, updater);

            } else {
                return renderFieldPanel(state, viewModel, updater);
            }
        }));
    }


    private static VTree renderFormSelection(PivotPageViewModel viewModel, PivotUpdater updater) {

        return FormSelectionView.render(viewModel.getFormSelection(), viewModel.getFieldList(), updater);

    }


    private static VTree renderFieldPanel(DesignPanelState state, PivotPageViewModel viewModel, PivotUpdater updater) {
        return new SidePanel()
                .expandButtonLabel(I18N.CONSTANTS.reportDesign())
                .hideMode(SidePanel.HideMode.COLLAPSE)
                .header(H.h3(I18N.CONSTANTS.reportDesign()))
                .content(renderFieldSelection())
                .expandedWidth("44rem")
                .expanded(state == DesignPanelState.VISIBLE, expanded ->
                        updater.update(s -> s.withPanelState(expanded ? DesignPanelState.VISIBLE : DesignPanelState.COLLAPSED)))
                .build();
    }

    private static VTree renderFieldSelection() {
        return H.div();
    }
}
