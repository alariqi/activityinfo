package org.activityinfo.ui.client.analysis.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.analysis.model.FormSelectionModel;
import org.activityinfo.ui.client.analysis.model.FormSelectionUpdater;
import org.activityinfo.ui.client.analysis.viewModel.FieldListViewModel;
import org.activityinfo.ui.client.analysis.viewModel.SelectedFieldViewModel;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class FieldListView {

    public static VTree render(FormStore formStore,
                               Observable<DesignMode> mode,
                               Observable<FormSelectionModel> model,
                               FormSelectionUpdater updater) {

        Observable<FieldListViewModel> viewModel = FieldListViewModel.compute(formStore, model);

        return div("fieldlist",
                    div("fieldlist__header",
                            h3(I18N.CONSTANTS.fields()),
                            toggleButton(viewModel, mode, updater)),
                    new ReactiveComponent(viewModel.transform(vm ->
                            ul(vm.getFields().stream().map(FieldListView::fieldItem)))));
    }

    private static VTree toggleButton(Observable<FieldListViewModel> viewModel,
                                      Observable<DesignMode> mode,
                                      FormSelectionUpdater updater) {

        Observable<Boolean> hasFields = viewModel
                .transform(fieldList -> !fieldList.getFields().isEmpty())
                .optimisticWithDefault(false);

        return new ReactiveComponent(Observable.transform(hasFields, mode, (enabled, m) -> {
            if(m == DesignMode.EXPANDED) {
                return Buttons.button(I18N.CONSTANTS.done())
                        .icon(Icon.BUBBLE_CHECKMARK)
                        .primary()
                        .block()
                        .enabled(enabled)
                        .onSelect(event -> updater.designMode(DesignMode.NORMAL))
                        .build();
            } else {
                return Buttons.button(I18N.CONSTANTS.manageFields())
                        .icon(Icon.BUBBLE_EDIT)
                        .primary()
                        .block()
                        .onSelect(event -> updater.designMode(DesignMode.EXPANDED))
                        .build();
            }
        }));
    }

    private static VTree fieldItem(SelectedFieldViewModel field) {

        PropMap itemProps = new PropMap().draggable(true);

        return H.li(itemProps,
                div("fieldlist__item__desc", t(field.getType() + " - " + field.getFormLabel())),
                div("fieldlist__item__label", t(field.getFieldLabel())));
    }
}
