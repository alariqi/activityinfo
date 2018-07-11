package org.activityinfo.ui.client.fields.view;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.fields.state.FieldChoiceUpdater;
import org.activityinfo.ui.client.fields.viewModel.FormSelectionColumn;
import org.activityinfo.ui.client.fields.viewModel.FormSelectionItem;
import org.activityinfo.ui.client.fields.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import static org.activityinfo.ui.vdom.shared.html.H.div;

public class FormSelectionView {



    public static VTree render(Observable<FormSelectionViewModel> viewModel,
                               FieldChoiceUpdater updater) {
        return new ReactiveComponent(
                viewModel.transform(vm ->
                        div("formselection",
                                vm.getColumns().stream().map(c -> FormSelectionView.column(updater, c)))));
    }

    private static VTree column(FieldChoiceUpdater updater, Observable<FormSelectionColumn> observableColumn) {
        return new ReactiveComponent(observableColumn.transform(column -> column(column, updater)));
    }

    private static VTree column(FormSelectionColumn column, FieldChoiceUpdater updater) {
        return div("formselection__column",
                column.getItems().stream().map(item -> item(column, item, updater)));
    }

    private static VNode item(FormSelectionColumn column, FormSelectionItem item, FieldChoiceUpdater updater) {

        PropMap itemProps = Props.create();
        itemProps.addClassName("formselection__item");
        itemProps.addClassName("formselection__item--selected", column.isSelected(item));
        itemProps.onclick(event -> updater.navigateToForm(column.getColumnIndex(), item.getId()));

        PropMap checkboxProps = Props.create();
        checkboxProps.addClassName("checkbox");
        checkboxProps.addClassName("checkbox--checked",
                    item.getSelected() == FormSelectionItem.Selection.ALL);

        checkboxProps.onclick(event -> updater.selectForm(item.getId()));

        VNode checkmark = H.div(checkboxProps, Icon.CHECKMARK.tree());

        return div(itemProps,
                H.div("formselection__item__inner",
                    checkmark,
                    new VText(item.getLabel())));
    }

}
