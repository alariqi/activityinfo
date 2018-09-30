package org.activityinfo.ui.client.reports.formSelection.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.Loader;
import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionUpdater;
import org.activityinfo.ui.client.reports.formSelection.viewModel.*;
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
                                vm.getColumns().stream().map(c -> FormSelectionView.column(vm, c, updater)))),
                         loadingIndicator());
    }

    private static VTree loadingIndicator() {
        return div("formselection",
                new Loader().small().dark().build());
    }

    private static VTree column(FormSelectionViewModel viewModel, SelectionColumn column, FormSelectionUpdater updater) {
        return div("formselection__column",
                column.getItems().stream().map(item -> item(viewModel, column, item, updater)));
    }

    private static VNode item(FormSelectionViewModel viewModel, SelectionColumn column, SelectionNode item, FormSelectionUpdater updater) {

        PropMap itemProps = Props.create();
        itemProps.addClassName("formselection__item");
        itemProps.addClassName("formselection__item--active", column.isActive(item));
        itemProps.addClassName("formselection__item--root", item.getType() == NodeType.ROOT);
        itemProps.addClassName("formselection__item--expandable", !item.isLeaf());
        itemProps.onclick(event -> updater.updateFormSelection(s -> s.navigateTo(column.getColumnIndex(), item.getId())));

        SelectionStatus selected = viewModel.isSelected(item.getId());
        VNode checkmark = new Svg()
                .setHref(treeIcon(selected))
                .setViewBox("0 0 20 20")
                .setClasses("formselection__checkmark")
                .onclick(event -> {
                    boolean addToSelection;
                    if(selected == SelectionStatus.ALL) {
                        addToSelection = false;
                    } else {
                        addToSelection = true;
                    }
                    updater.selectForms(item.findForms(), addToSelection);
                })
                .build();

        VTree surtitle;
        if(item.getType() != NodeType.ROOT) {
            surtitle = H.div("surtitle", new VText(surtitle(item)));
        } else {
            surtitle = null;
        }

        VNode expandIcon = null;
        if(!item.isLeaf()) {
            expandIcon = new Svg()
                    .setHref("#tree_expand")
                    .setViewBox("0 0 10 10")
                    .setClasses("formselection__expand")
                    .build();
        }

        return div(itemProps,
                H.div("formselection__item__inner",
                    nullableList(
                        checkmark,
                        surtitle,
                        new VText(item.getLabel()),
                        expandIcon)));
    }

    private static String treeIcon(SelectionStatus status) {
        switch (status) {
            case ALL:
                return "#tree_selected";
            case SOME:
                return "#tree_partial";
            default:
                return "#tree_unselected";
        }
    }

    private static String surtitle(SelectionNode item) {
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
