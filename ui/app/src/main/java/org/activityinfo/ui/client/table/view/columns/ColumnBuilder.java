package org.activityinfo.ui.client.table.view.columns;

import org.activityinfo.analysis.table.Sorting;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.client.base.datatable.DataTableColumn;
import org.activityinfo.ui.client.table.viewModel.ColumnHeaderViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

public class ColumnBuilder {
    private final Observable<ColumnHeaderViewModel> viewModel;
    private VTree heading;
    private int width = 150;

    public static ColumnBuilder build(Observable<ColumnHeaderViewModel> header) {
        return new ColumnBuilder(header);
    }

    public ColumnBuilder(Observable<ColumnHeaderViewModel> viewModel) {
        this.viewModel = viewModel;
    }

    public ColumnBuilder heading(String heading) {
        PropMap props = Props.withClass("datatable__colheader__label");
        props.setTitle(heading);

        this.heading = new VNode(HtmlTag.DIV, props, new VText(heading));
        return this;
    }

    public ColumnBuilder heading(String surtitle, String heading) {

        PropMap props = Props.withClass("datatable__colheader__label");
        props.setTitle(heading);

        this.heading = new VNode(HtmlTag.DIV, props,
                H.div("surtitle", new VText(I18N.CONSTANTS.subForm())),
                new VText(heading));

        return this;
    }

    public DataTableColumn build() {
        return new DataTableColumn(width, render());
    }

    public VTree render() {
        return new ReactiveComponent(viewModel.transform(h -> {

            Style headerStyle = new Style();
            headerStyle.set("width", width + "px");

            PropMap thProps = Props.create();
            thProps.setStyle(headerStyle);

            PropMap colHeaderProps = Props.create();
            colHeaderProps.addClassName("datatable__colheader");
            colHeaderProps.setStyle(headerStyle);

            List<VTree> children = new ArrayList<>();

            children.add(heading);

            if(h.getSortOrder() != Sorting.NONE) {
                children.add(sortIcon(h.getSortOrder()));
            }

            children.add(filterIcon(h.isFiltered()));

            return new VNode(HtmlTag.TH, thProps,
                    new VNode(HtmlTag.DIV, colHeaderProps, children));
        }));
    }

    private static VTree sortIcon(Sorting sorting) {
        return Svg.svg("sorticon", sorting == Sorting.ASCENDING ?
                "#sort_ascending" :
                "#sort_descending", "0 0 12 15");
    }

    private static VTree filterIcon(boolean active) {
        return Svg.svg("datatable__colheader__filter", active ?
                "#filter_active" :
                "#filter_inactive", "0 0 26 25");
    }
}
