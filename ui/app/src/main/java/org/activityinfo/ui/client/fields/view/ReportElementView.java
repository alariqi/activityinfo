package org.activityinfo.ui.client.fields.view;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.fields.state.FieldChoiceUpdater;
import org.activityinfo.ui.client.fields.viewModel.FieldChoiceViewModel;
import org.activityinfo.ui.client.fields.viewModel.ReportElement;
import org.activityinfo.ui.client.fields.viewModel.ReportElementGroup;
import org.activityinfo.ui.client.fields.viewModel.ReportElementListView;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.*;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class ReportElementView {

    /**
     * Renders the list of selected fields/measures/layers/etc
     */
    public static VTree render(FieldChoiceViewModel viewModel, FieldChoiceUpdater updater) {
        return div("fieldlist",
                nullableList(
                    selectedHeader(viewModel),
                    reportElementList(viewModel.getReportElements())));
    }

    private static VTree selectedHeader(FieldChoiceViewModel viewModel) {
        if(viewModel.getReportElementHeader().isPresent()) {
            return div("fieldlist__header", h3(viewModel.getReportElementHeader().get()));
        }

        return null;
    }

    public static VTree reportElementList(Observable<ReportElementListView> viewModel) {
        return new ReactiveComponent(viewModel.transform(vm ->
                div("fieldlist__body",
                        vm.getGroups().stream().map(ReportElementView::elementGroup))));
    }

    private static VTree elementGroup(ReportElementGroup group) {

        PropMap groupProps = Props.withClass("fieldlist__group");

        VNode list = ul(group.getItems().stream().map(ReportElementView::reportItem));

        if(group.hasHeader()) {
            return H.div("fieldlist__group", h3(group.getHeading()), list);
        } else {
            return H.div("fieldlist__group", list);
        }
    }


    private static VTree reportItem(ReportElement element) {

        PropMap itemProps = Props.create()
                .setClass("fieldlist__item");


        return H.li(itemProps,
                div("surtitle", t(element.getType() + " - " + element.getFormLabel())),
                div("fieldlist__item__label", t(element.getLabel())));
    }


}
