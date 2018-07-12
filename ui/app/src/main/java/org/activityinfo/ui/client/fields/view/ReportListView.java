package org.activityinfo.ui.client.fields.view;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import jsinterop.base.Js;
import org.activityinfo.ui.client.fields.state.DropTarget;
import org.activityinfo.ui.client.fields.state.FieldChoiceUpdater;
import org.activityinfo.ui.client.fields.viewModel.FieldChoiceViewModel;
import org.activityinfo.ui.client.fields.viewModel.ReportElement;
import org.activityinfo.ui.client.fields.viewModel.ReportElementGroup;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class ReportListView {

    private static final Logger LOGGER = Logger.getLogger(ReportListView.class.getName());

    /**
     * Renders the list of selected fields/measures/layers/etc
     */
    public static VTree render(FieldChoiceViewModel viewModel, FieldChoiceUpdater updater) {
        return div("fieldlist",
                nullableList(
                    selectedHeader(viewModel),
                    reportElementList(viewModel, updater)));
    }

    private static VTree selectedHeader(FieldChoiceViewModel viewModel) {
        if(viewModel.getReportElementHeader().isPresent()) {
            return div("fieldlist__header", h3(viewModel.getReportElementHeader().get()));
        }

        return null;
    }

    public static VTree reportElementList(FieldChoiceViewModel viewModel, FieldChoiceUpdater updater) {
        return new ReactiveComponent(viewModel.getReportElements().transform(vm ->
                div("fieldlist__body",
                        vm.getGroups().stream().map(group -> {
                            return elementGroup(group, updater);
                        }))));
    }

    private static VTree elementGroup(ReportElementGroup group, FieldChoiceUpdater updater) {

        PropMap groupProps = Props.withClass("fieldlist__group");
        groupProps.ondragover(event -> onDragOver(event, group, updater));
        groupProps.ondragleave(event -> onDragLeave(updater));
        groupProps.ondrop(event -> onDrop(event, group, updater));

        List<VTree> listItems = new ArrayList<>();
        List<ReportElement> items = group.getItems();
        for (int i = 0; i < items.size(); i++) {
            if(i == group.getDropInsertIndex()) {
                listItems.add(dropPlaceholder(i));
            }
            listItems.add(reportItem(i, items.get(i)));
        }

        VNode ul = H.ul(listItems.stream());

        if(group.hasHeader()) {
            return H.div(groupProps, h3(group.getHeading()), ul);
        } else {
            return H.div(groupProps, ul);
        }
    }



    private static VTree dropPlaceholder(int index) {
        PropMap propMap = Props.create()
                .setClass("fieldlist__item fieldlist__item--placeholder")
                .data("index", "" +index);

        return H.div(propMap);
    }

    private static VTree reportItem(int index, ReportElement element) {

        PropMap itemProps = Props.create()
                .setClass("fieldlist__item")
                .setData("index", "" + index);

        return H.li(itemProps,
                div("surtitle", t(element.getType() + " - " + element.getFormLabel())),
                div("fieldlist__item__label", t(element.getLabel())));
    }

    private static void onDragOver(Event event, ReportElementGroup group, FieldChoiceUpdater updater) {

        // Allows drop
        elemental2.dom.DragEvent dragEvent = Js.cast(event);
        dragEvent.preventDefault();

        DropTarget dropTarget = findDropTarget(group, event);
        updater.update(s -> s.withDropTarget(dropTarget));
    }


    private static void onDragLeave(FieldChoiceUpdater updater) {
        updater.update(s -> s.withDropTarget(Optional.empty()));
    }

    private static void onDrop(Event event, ReportElementGroup group, FieldChoiceUpdater updater) {

        // Prevent browser from redirecting
        elemental2.dom.DragEvent dragEvent = Js.cast(event);
        dragEvent.stopPropagation();
        dragEvent.preventDefault();

        DropTarget dropTarget = findDropTarget(group, event);
        updater.drop(dropTarget);
    }


    private static DropTarget findDropTarget(ReportElementGroup group, Event event) {
        // Find the index of the over position

        Element element = event.getEventTarget().cast();
        if(element.hasClassName("fieldlist__group")) {
            return new DropTarget(group.getId(), 0);
        }

        while(!element.hasClassName("fieldlist__item")) {
            element = element.getParentElement();
            if(element == null) {
                return new DropTarget(group.getId(), 0);
            }
        }

        int itemIndex = Integer.parseInt(element.getAttribute("data-index"));

        elemental2.dom.DragEvent dragEvent = Js.cast(event.cast());
        int midpoint = element.getOffsetTop() + element.getOffsetHeight() / 2;

        if(dragEvent.offsetY > midpoint) {
            itemIndex ++;
        }
        return new DropTarget(group.getId(), itemIndex);
    }

}
