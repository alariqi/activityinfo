package org.activityinfo.ui.client.fields.view;

import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.fields.model.DesignMode;
import org.activityinfo.ui.client.fields.model.FieldChoiceUpdater;
import org.activityinfo.ui.client.fields.viewModel.FieldChoiceViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import static org.activityinfo.ui.vdom.shared.html.H.div;

public class FieldChoiceView {

    private FieldChoiceViewModel viewModel;
    private FieldChoiceUpdater updater;

    public static VTree render(FieldChoiceViewModel viewModel, FieldChoiceUpdater updater) {

        VTree formList = FormSelectionView.render(viewModel.getFormSelection(), updater);
        VTree availableList = FieldListView.available(viewModel, updater);
        VTree selectedList = FieldListView.selected(viewModel, updater);

        return new ReactiveComponent(viewModel.getMode().transform(m ->
                new SidePanel()
                .title(new VText(viewModel.getPanelTitle()))
                .header(H.h2(viewModel.getPanelTitle()))
                .expanded(viewModel.isExpanded(), updater::expandPanel)
                .hideMode(viewModel.getPanelHideMode())
                .leftSide()
                .full(m == DesignMode.EXPANDED)
                .expandedWidth("45rem")
                .content(m == DesignMode.EXPANDED ?
                        div("fieldpanel", formList, availableList) :
                        div("fieldpanel", availableList, selectedList))
                .build()));

    }
}
