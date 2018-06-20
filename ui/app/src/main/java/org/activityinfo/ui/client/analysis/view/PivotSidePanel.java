package org.activityinfo.ui.client.analysis.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.analysis.model.FormSelectionModel;
import org.activityinfo.ui.client.analysis.model.FormSelectionUpdater;
import org.activityinfo.ui.client.analysis.viewModel.AnalysisViewModel;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import static org.activityinfo.ui.vdom.shared.html.H.div;

public class PivotSidePanel {

    public static VTree render(FormStore formStore) {

        StatefulValue<DesignMode> mode = new StatefulValue<>(DesignMode.EXPANDED);
        StatefulValue<Boolean> expanded = new StatefulValue<>(true);
        StatefulValue<FormSelectionModel> formModel = new StatefulValue<>(new FormSelectionModel());

        FormSelectionUpdater updater = new FormSelectionUpdater() {
            @Override
            public void navigateTo(int columnIndex, ResourceId itemId) {
                formModel.updateValue(formModel.get().navigateTo(columnIndex, itemId));
            }

            @Override
            public void select(ResourceId id) {
                formModel.updateValue(formModel.get().toggleSelection(id));
            }

            @Override
            public void designMode(DesignMode newMode) {
                mode.updateIfNotSame(newMode);
            }
        };

        VTree formList = FormSelectionView.render(formStore, formModel, updater);
        VTree fieldList = FieldListView.render(formStore, mode, formModel, updater);
        VTree dimensions = DimensionView.render(new AnalysisViewModel(formStore, "xyz"));

        return new ReactiveComponent(mode.transform(m ->
                new SidePanel()
                .title(new VText(I18N.CONSTANTS.reportDesign()))
                .header(H.h2(I18N.CONSTANTS.reportDesign()))
                .expanded(expanded)
                .leftSide()
                .full(m == DesignMode.EXPANDED)
                .expandedWidth("46rem")
                .content(m == DesignMode.EXPANDED ?
                        div("pivotdesign", formList, fieldList) :
                        div("pivotdesign", fieldList, dimensions))
                .build()));

    }
}
