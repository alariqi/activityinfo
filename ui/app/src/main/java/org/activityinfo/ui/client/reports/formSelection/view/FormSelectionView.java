package org.activityinfo.ui.client.reports.formSelection.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionUpdater;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionColumn;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionItem;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import static org.activityinfo.ui.vdom.shared.html.H.div;
import static org.activityinfo.ui.vdom.shared.html.H.nullableList;

public class FormSelectionView {



    public static VTree render(Observable<FormSelectionViewModel> viewModel,
                               FormSelectionUpdater updater) {
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
        itemProps.onclick(event -> updater.updateFormSelection(s -> s.navigateTo(column.getColumnIndex(), item.getId())));

        PropMap checkboxProps = Props.create();
        checkboxProps.addClassName("checkbox");
        checkboxProps.addClassName("checkbox--checked",
                    item.getSelected() == FormSelectionItem.Selection.ALL);

        checkboxProps.onclick(event -> updater.updateFormSelection(s -> s.toggleSelection(item.getId())));

        VNode checkmark = H.div(checkboxProps, Icon.CHECKMARK.tree());

        VTree surtitle;
        if(item.getType() != FormSelectionItem.ItemType.ROOT) {
            surtitle = H.div("surtitle", new VText(surtitle(item)));
        } else {
            surtitle = null;
        }

        return div(itemProps,
                H.div("formselection__item__inner",
                    nullableList(
                        checkmark,
                        surtitle,
                        new VText(item.getLabel()))));
    }

    private static String surtitle(FormSelectionItem item) {
        switch (item.getType()) {
            case DATABASE:
                return I18N.CONSTANTS.database();
            case FOLDER:
                return I18N.CONSTANTS.folder();
            default:
            case FORM:
                return I18N.CONSTANTS.form();
            case SUBFORM:
                return I18N.CONSTANTS.subForm();
        }
    }

}
