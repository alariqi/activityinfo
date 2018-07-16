package org.activityinfo.ui.client.base.datatable;

import elemental2.dom.*;
import jsinterop.base.Js;
import org.activityinfo.analysis.table.Sorting;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class DataTable {


    private static final int BUFFER_SIZE = 50;

    private static final Logger LOGGER = Logger.getLogger(DataTable.class.getName());

    private Observable<List<DataTableColumn>> columns;
    private Function<Observable<RowRange>, Observable<TableSlice>> rowRenderer;
    private RowClickHandler rowClickHandler;

    /**
     * The height of the row in pixels
     */
    private double rowHeight = 44;

    private StatefulValue<RowRange> range = new StatefulValue<>(new RowRange(0, BUFFER_SIZE));

    public DataTable() {
    }

    public DataTable setColumns(Observable<List<DataTableColumn>> columns) {
        this.columns = columns;
        return this;
    }

    public DataTable setRowRenderer(Function<Observable<RowRange>, Observable<TableSlice>> rowRenderer) {
        this.rowRenderer = rowRenderer;
        return this;
    }


    public DataTable setRowClickHandler(RowClickHandler handler) {
        this.rowClickHandler = handler;
        return this;
    }

    public VTree build() {

        VNode loading = render(Collections.emptyList());
        Observable<VTree> rendered = columns.transform(c -> render(c));

        return new Component(
                new ReactiveComponent(
                        rendered,
                        loading));
    }

    private VNode render(List<DataTableColumn> c) {
        return div("datatable",
                div("datatable__inner",
                    renderHeader(c),
                    renderBody(c)));
    }


    private VTree renderHeader(List<DataTableColumn> columns) {
        return div("datatable__header",
            table(tableProps(columns),
                tableHead(
                    tableRow(columns.stream().map(this::renderColumnHeader))
                )));
    }

    private VTree renderColumnHeader(DataTableColumn column) {
        Style headerStyle = new Style();
        headerStyle.set("width", column.getWidth() + "px");

        PropMap thProps = Props.create();
        thProps.setStyle(headerStyle);

        PropMap colHeaderProps = Props.create();
        colHeaderProps.addClassName("datatable__colheader");
        colHeaderProps.setStyle(headerStyle);

        List<VTree> children = new ArrayList<>();

        PropMap labelProps = Props.withClass("datatable__colheader__label");
        labelProps.setTitle(column.getHeading());

        if(column.hasSurtitle()) {
            children.add( new VNode(HtmlTag.DIV, labelProps,
                    H.div("surtitle", new VText(I18N.CONSTANTS.subForm())),
                    new VText(column.getHeading())));
        } else {
            children.add(new VNode(HtmlTag.DIV, labelProps, new VText(column.getHeading())));
        }

        if(column.getSorting() != Sorting.NONE) {
            children.add(sortIcon(column.getSorting()));
        }

        children.add(filterIcon(column.isFilterActive()));

        return new VNode(HtmlTag.TH, thProps,
                new VNode(HtmlTag.DIV, colHeaderProps, children));
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

    private VTree renderBody(List<DataTableColumn> columns) {

        Observable<RowRange> debouncedRange = this.range.debounce(100);
        Observable<TableSlice> tableSlice = rowRenderer.apply(debouncedRange);

        VTree loading = renderBodyInner(columns, TableSlice.EMPTY);
        Observable<VTree> rendered = tableSlice.transform(s -> renderBodyInner(columns, s));

        return new ReactiveComponent(rendered, loading);
    }

    private VTree renderBodyInner(List<DataTableColumn> columns, TableSlice slice) {

        // The datatable__body__inner div defines the overall (total) size of the table
        PropMap innerStyle = Props.create();
        innerStyle.set("width", totalWidth(columns) + "px");
        innerStyle.set("height", (slice.getTotalRowCount() * rowHeight) + "px");

        PropMap innerProps = Props.create();
        innerProps.setStyle(innerStyle);
        innerProps.setClass("datatable__body__inner");

        // The datatable__body__inner table contains only the rows we have rendered
        // and is positioned absolutely as needed

        PropMap tableStyle = Props.create();
        tableStyle.set("width", totalWidth(columns) + "px");
        tableStyle.set("top", (slice.getStartIndex() * rowHeight) + "px");

        PropMap tableProps = Props.create();
        tableProps.setStyle(tableStyle);

        if(rowClickHandler != null) {
            tableProps.onclick(event -> onBodyClick(event));
        }

        return div("datatable__body",
                div(innerProps,
                    table(tableProps,
                        renderBodyHeaders(columns),
                        slice.getTableBody())));
    }

    private void onBodyClick(com.google.gwt.user.client.Event event) {
        Element element = Js.cast(event.getEventTarget());
        Element row = element.closest("tr[data-row]");
        if(row != null) {
            rowClickHandler.onClick(row.getAttribute("data-row"));
        }
    }

    private PropMap tableProps(List<DataTableColumn> columns) {

        PropMap style = Props.create();
        style.set("width", totalWidth(columns) + "px");

        PropMap tableProps = Props.create();
        tableProps.setStyle(style);
        tableProps.set("cellpadding", "0");
        tableProps.set("cellspacing", "0");
        return tableProps;
    }

    private int totalWidth(List<DataTableColumn> columns) {
        int sum = 0;
        for (DataTableColumn column : columns) {
            sum += column.width;
        }
        return sum;
    }


    /**
     * Render a table head section for the body table in order to keep the column widths
     * fixed and in sync with the column headers.
     * @param columns
     */
    private VTree renderBodyHeaders(List<DataTableColumn> columns) {
        return tableHead(
                tableRow(columns.stream().map(this::renderBodyHeader)));
    }

    private VTree renderBodyHeader(DataTableColumn dataTableColumn) {
        Style headerStyle = new Style();
        headerStyle.set("width", dataTableColumn.getWidth() + "px");
        headerStyle.set("height", "0px");

        PropMap headerProps = Props.create();
        headerProps.setStyle(headerStyle);

        return new VNode(HtmlTag.TH, headerProps);
    }


    private class Component extends VComponent {

        private final VTree content;
        private ScrollListener eventListener;

        private Component(VTree content) {
            this.content = content;
        }

        @Override
        protected VTree render() {
            return content;
        }

        private Element getContainer() {
            return Js.cast(getDomNode());
        }

        @Override
        protected void componentDidMount() {
            Element container = getContainer();
            Element tableBody = container.querySelector(".datatable__body");
            Element headerTable = container.querySelector(".datatable__header table");
            eventListener = new ScrollListener(tableBody, headerTable);
            eventListener.addListener();
        }

        @Override
        protected void componentWillUnmount() {
            eventListener.removeListener();
            eventListener = null;
        }
    }

    /**
     * Listens for changes to the body's scroll area, and syncs the top header bar and maybe
     */
    private class ScrollListener implements EventListener {
        private final Element tableBody;
        private final HTMLElement headerTable;

        private boolean measuredRowHeight = false;

        public ScrollListener(Element tableBody, Element headerTable) {
            this.tableBody = tableBody;
            this.headerTable = (HTMLElement) headerTable;
        }

        @Override
        public void handleEvent(Event evt) {
            // Sync the header scrolling
            headerTable.style.left = (-tableBody.scrollLeft) + "px";

            // Update our estimate of row height with a measured value
            if(!measuredRowHeight) {
                measureRowHeight();
            }

            // Determine whether our range needs to be update
            RowRange visibleRange = RowRange.fromScroll(tableBody.scrollTop, tableBody.clientHeight, rowHeight);
            RowRange renderedRange = range.get();

            if(!renderedRange.contains(visibleRange)) {
                LOGGER.info("DataTable: Updating visible range");
                range.updateIfNotSame(visibleRange.withRowCount(BUFFER_SIZE));
            }
        }

        private void measureRowHeight() {
            Element firstRow = tableBody.querySelector("tbody tr");
            if(firstRow != null) {
                ClientRect clientRect = firstRow.getBoundingClientRect();
                rowHeight = clientRect.height;
                measuredRowHeight = true;

                LOGGER.info("Measured row height = " + rowHeight);
            }
        }


        public void addListener() {
            tableBody.addEventListener("scroll", this);
        }

        public void removeListener() {
            tableBody.removeEventListener("scroll", this);
        }
    }
}
