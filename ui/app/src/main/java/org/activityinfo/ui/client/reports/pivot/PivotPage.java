package org.activityinfo.ui.client.reports.pivot;

import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionState;
import org.activityinfo.ui.client.reports.pivot.state.PivotState;
import org.activityinfo.ui.client.reports.pivot.state.PivotUpdater;
import org.activityinfo.ui.client.reports.pivot.view.PivotView;
import org.activityinfo.ui.client.reports.pivot.viewModel.PivotPageViewModel;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.function.Function;

public class PivotPage extends Page {

    private final FormStore formStore;
    private final PivotPlace place;

    public PivotPage(FormStore formStore, PivotPlace place) {
        this.formStore = formStore;
        this.place = place;
    }

    @Override
    public VTree render() {

        StatefulValue<PivotState> state = new StatefulValue<>(new PivotState());
        PivotUpdater updater = new PivotUpdater() {
            @Override
            public void update(Function<PivotState, PivotState> function) {
                state.update(function);
            }

            @Override
            public void updateFormSelection(Function<FormSelectionState, FormSelectionState> function) {
                state.update(s -> s.withFormSelection(function.apply(s.getFormSelection())));
            }
        };
        PivotPageViewModel viewModel = new PivotPageViewModel(formStore, state);

        return AppFrame.render(formStore, PivotView.render(viewModel, updater));
    }
}
