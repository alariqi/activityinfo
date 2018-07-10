package org.activityinfo.ui.client.fields.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.fields.model.DesignMode;
import org.activityinfo.ui.client.fields.model.FieldChoiceUpdater;
import org.activityinfo.ui.client.fields.viewModel.FieldChoiceViewModel;
import org.activityinfo.ui.client.fields.viewModel.FieldListGroup;
import org.activityinfo.ui.client.fields.viewModel.FieldListItem;
import org.activityinfo.ui.client.fields.viewModel.FieldListViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class FieldListView {


    /**
     * Renders the list of available fields.
     */
    public static VTree available(FieldChoiceViewModel viewModel,
                                  FieldChoiceUpdater updater) {

        return div("fieldlist",
                 header(viewModel, updater),
                 fieldList(viewModel.getAvailableFields()));
    }

    private static VNode header(FieldChoiceViewModel viewModel, FieldChoiceUpdater updater) {
        return div("fieldlist__header",
           nullableList(
               h3(I18N.CONSTANTS.fields()),
               toggleButton(viewModel, updater)));
    }


    /**
     * Renders the list of selected fields/measures/layers/etc
     */
    public static VTree selected(FieldChoiceViewModel viewModel, FieldChoiceUpdater updater) {
        return div("fieldlist",
                nullableList(
                    selectedHeader(viewModel),
                    fieldList(viewModel.getSelectedFields())));
    }

    private static VTree selectedHeader(FieldChoiceViewModel viewModel) {
        if(viewModel.getSelectedFieldsHeading().isPresent()) {
            return div("fieldlist__header", h3(viewModel.getSelectedFieldsHeading().get()));
        }

        return null;
    }

    /**
     * Button to toggle between choosing forms and assigning fields to the report.
     */
    private static VTree toggleButton(FieldChoiceViewModel viewModel,
                                      FieldChoiceUpdater updater) {

        if(!viewModel.isFormSelectionEnabled()) {
            return null;
        }

        Observable<Boolean> hasFields = viewModel.getAvailableFields()
                .transform(fieldList -> !fieldList.isEmpty())
                .optimisticWithDefault(false);

        return new ReactiveComponent(Observable.transform(hasFields, viewModel.getMode(), (enabled, m) -> {
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

    public static VTree fieldList(Observable<FieldListViewModel> viewModel) {
        return new ReactiveComponent(viewModel.transform(vm ->
                div("fieldlist__body darkscroll",
                        vm.getGroups().stream().map(FieldListView::fieldGroup))));
    }

    private static VTree fieldGroup(FieldListGroup group) {


        VNode list = ul(group.getItems().stream().map(FieldListView::fieldItem));

        if(group.hasHeader()) {
            return H.div("fieldlist__group", h3(group.getHeading()), list);
        } else {
            return H.div("fieldlist__group", list);
        }
    }

    private static VTree fieldItem(FieldListItem field) {

        PropMap itemProps = Props.create().draggable(true).setClass("fieldlist__item");

        if(field.isIncluded()) {
            itemProps.addClassName("fieldlist__item--included");
        }

        return H.li(itemProps,
                div("fieldlist__item__desc", t(field.getType() + " - " + field.getFormLabel())),
                div("fieldlist__item__label", t(field.getFieldLabel())));
    }

}
