package org.activityinfo.ui.client.analysis.view;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.analysis.model.FormSelectionModel;
import org.activityinfo.ui.client.analysis.model.FormSelectionUpdater;
import org.activityinfo.ui.client.analysis.viewModel.FormSelectionColumn;
import org.activityinfo.ui.client.analysis.viewModel.FormSelectionItem;
import org.activityinfo.ui.client.analysis.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import static org.activityinfo.ui.vdom.shared.html.H.div;

public class FormSelectionView {

    public static VTree render(FormStore formStore,
                               final Observable<FormSelectionModel> model,
                               FormSelectionUpdater updater) {

        Observable<FormSelectionViewModel> viewModel =
                model.transform(m -> FormSelectionViewModel.compute(formStore, m));

        return new ReactiveComponent(
                viewModel.transform(vm ->
                        div("formselection",
                                vm.getColumns().stream().map(c -> FormSelectionView.column(updater, c)))));
    }


    private static VTree column(FormSelectionUpdater updater, Observable<FormSelectionColumn> observableColumn) {
        return new ReactiveComponent(observableColumn.transform(column -> column(column, updater)));
    }

    private static VTree column(FormSelectionColumn column, FormSelectionUpdater updater) {
        return div("formselection__column",
                column.getItems().stream().map(item -> item(column, item, updater)));
    }

    private static VNode item(FormSelectionColumn column, FormSelectionItem item, FormSelectionUpdater updater) {

        PropMap itemProps = Props.create();
        itemProps.addClassName("formselection__item");
        itemProps.addClassName("formselection__item--selected", column.isSelected(item));
        itemProps.onclick(event -> updater.navigateTo(column.getColumnIndex(), item.getId()));

        PropMap checkboxProps = Props.create();
        checkboxProps.addClassName("checkbox");
        checkboxProps.addClassName("checkbox--checked",
                    item.getSelected() == FormSelectionItem.Selection.ALL);

        checkboxProps.onclick(event -> updater.select(item.getId()));

        VNode checkmark = H.div(checkboxProps, Icon.CHECKMARK.tree());

        return div(itemProps,
                H.div("formselection__item__inner",
                    checkmark,
                    new VText(item.getLabel())));
    }

}
