package org.activityinfo.ui.client.fields.view;

import com.google.gwt.user.client.Event;
import elemental2.dom.DragEvent;
import jsinterop.base.Js;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.fields.state.DesignMode;
import org.activityinfo.ui.client.fields.state.FieldChoiceUpdater;
import org.activityinfo.ui.client.fields.viewModel.FieldChoiceViewModel;
import org.activityinfo.ui.client.fields.viewModel.FieldListGroup;
import org.activityinfo.ui.client.fields.viewModel.FieldListItem;
import org.activityinfo.ui.client.fields.viewModel.FieldListViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.logging.Logger;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class FieldListView {

    private static final Logger LOGGER = Logger.getLogger(FieldListView.class.getName());


    /**
     * Renders the list of available fields.
     */
    public static VTree available(FieldChoiceViewModel viewModel,
                                  FieldChoiceUpdater updater) {

        return div("fieldlist",
                 header(viewModel, updater),
                 fieldList(viewModel.getAvailableFields(), updater));
    }

    private static VNode header(FieldChoiceViewModel viewModel, FieldChoiceUpdater updater) {
        return div("fieldlist__header",
           nullableList(
               h3(I18N.CONSTANTS.fields()),
               toggleButton(viewModel, updater)));
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
                        .onSelect(event -> updater.update(s -> s.withMode(DesignMode.NORMAL)))
                        .build();
            } else {
                return Buttons.button(I18N.CONSTANTS.manageFields())
                        .icon(Icon.BUBBLE_EDIT)
                        .primary()
                        .block()
                        .onSelect(event -> updater.update(s -> s.withMode(DesignMode.EXPANDED)))
                        .build();
            }
        }));
    }

    public static VTree fieldList(Observable<FieldListViewModel> viewModel, FieldChoiceUpdater updater) {
        return new ReactiveComponent(viewModel.transform(vm ->
                div("fieldlist__body",
                        vm.getGroups().stream().map(group -> {
                            return fieldGroup(vm, group, updater);
                        }))));
    }

    private static VTree fieldGroup(FieldListViewModel listView, FieldListGroup group, FieldChoiceUpdater updater) {

        VNode list = ul(group.getItems().stream().map(field -> fieldItem(listView, field, updater)));

        if(group.hasHeader()) {
            return H.div("fieldlist__group", h3(group.getHeading()), list);
        } else {
            return H.div("fieldlist__group", list);
        }
    }

    private static VTree fieldItem(FieldListViewModel listView, FieldListItem field, FieldChoiceUpdater updater) {

        PropMap itemProps = Props.create()
                .draggable(true)
                .ondragstart(event -> onDragStart(event, field, updater))
                .ondragend(event -> onDragEnd(updater))
                .setClass("fieldlist__item");

        if(listView.isDragging(field)) {
            itemProps.addClassName("fieldlist__item--dragging");
        }

        if(field.isIncluded()) {
            itemProps.addClassName("fieldlist__item--included");
        }

        return H.li(itemProps,
                div("surtitle", t(field.getType() + " - " + field.getFormLabel())),
                div("fieldlist__item__label", t(field.getFieldLabel())));
    }

    private static void onDragStart(Event event, FieldListItem field, FieldChoiceUpdater updater) {

        LOGGER.info("FieldListView: onDragStart");

        DragEvent dragEvent = Js.cast(event);
        dragEvent.dataTransfer.setData("text/plain", field.getFormula());
        dragEvent.dataTransfer.dropEffect = "copy";

        updater.update(s -> s.fieldDragStart(field.getFormula()));
    }

    private static void onDragEnd(FieldChoiceUpdater updater) {
        updater.update(s -> s.draggingEnd());
    }

}
