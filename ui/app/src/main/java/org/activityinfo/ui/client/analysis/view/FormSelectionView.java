package org.activityinfo.ui.client.analysis.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.analysis.model.FormSelectionModel;
import org.activityinfo.ui.client.analysis.model.FormSelectionUpdater;
import org.activityinfo.ui.client.analysis.viewModel.FormSelectionColumn;
import org.activityinfo.ui.client.analysis.viewModel.FormSelectionItem;
import org.activityinfo.ui.client.analysis.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import static org.activityinfo.ui.vdom.shared.html.H.div;

public class FormSelectionView {

    public static VTree render(FormStore formStore) {

        StatefulValue<FormSelectionModel> model = new StatefulValue<>(new FormSelectionModel());
        Observable<FormSelectionViewModel> viewModel = model.transform(m -> FormSelectionViewModel.compute(formStore, m));

        FormSelectionUpdater updater = new FormSelectionUpdater() {
            @Override
            public void navigateTo(int columnIndex, ResourceId itemId) {
                model.updateValue(model.get().navigateTo(columnIndex, itemId));
            }

            @Override
            public void select(ResourceId id) {
                model.updateValue(model.get().toggleSelection(id));
            }
        };

        VTree formSelection = new ReactiveComponent("formSelection",
                viewModel.transform(vm ->
                        div("formselection",
                            div("formselection__forms",
                                vm.getColumns().stream().map(c -> FormSelectionView.column(updater, c))),
                            div("formselection__fields",
                                H.h3(I18N.CONSTANTS.fields())))));

        VTree sidePanel = new SidePanel()
                .title(new VText(I18N.CONSTANTS.reportDesign()))
                .header(H.h2(I18N.CONSTANTS.reportDesign()))
                .leftSide()
                .full()
                .content(formSelection)
                .build();

        return sidePanel;
    }

    private static VTree column(FormSelectionUpdater updater, Observable<FormSelectionColumn> observableColumn) {
        return new ReactiveComponent("col", observableColumn.transform(column -> {
            return column(column, updater);
        }));
    }

    private static VTree column(FormSelectionColumn column, FormSelectionUpdater updater) {
        return div("formselection__column",
                column.getItems().stream().map(item -> item(column, item, updater)));
    }

    private static VNode item(FormSelectionColumn column, FormSelectionItem item, FormSelectionUpdater updater) {

        PropMap itemProps = new PropMap();
        itemProps.addClassName("formselection__item");
        itemProps.addClassName("formselection__item--selected", column.isSelected(item));
        itemProps.onclick(event -> updater.navigateTo(column.getColumnIndex(), item.getId()));

        PropMap checkboxProps = new PropMap();
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
