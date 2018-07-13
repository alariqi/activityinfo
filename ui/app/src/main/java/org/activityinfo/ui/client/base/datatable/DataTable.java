package org.activityinfo.ui.client.base.datatable;

import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Subscription;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DataTable {
    private Observable<List<DataTableColumn>> columns;
    private Observable<Integer> rowCount;
    private Function<Observable<RowRange>, Observable<VTree>> rowRenderer;
    private Observable<String> selectedRow = Observable.just("");
    private RowClickHandler rowClickHandler;

    public DataTable() {
    }

    public DataTable setColumns(Observable<List<DataTableColumn>> columns) {
        this.columns = columns;
        return this;
    }

    public DataTable setRowCount(Observable<Integer> rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    public DataTable setRowRenderer(Function<Observable<RowRange>, Observable<VTree>> rowRenderer) {
        this.rowRenderer = rowRenderer;
        return this;
    }


    public DataTable setRowClickHandler(RowClickHandler handler) {
        this.rowClickHandler = handler;
        return this;
    }

    public DataTable setSelectedRow(Observable<String> selection) {
        this.selectedRow = selection;
        return this;
    }

    public VTree build() {
        return new Component(
                new ReactiveComponent(
                    columns.transform(c -> render(c)),
                    render(Collections.emptyList())));
    }

    private VNode render(List<DataTableColumn> c) {
        return H.div("datatable",
            H.div("datatable__inner",
                    renderHeader(c),
                    renderBody(c)));
    }


    private VTree renderHeader(List<DataTableColumn> columns) {
        return H.div("datatable__header",
            H.table(tableProps(columns),
                H.tableHead(
                        H.tableRow(columns.stream().map(this::renderColumnHeader))
                )));
    }

    private VTree renderColumnHeader(DataTableColumn dataTableColumn) {
        Style headerStyle = new Style();
        headerStyle.set("width", dataTableColumn.getWidth() + "px");

        PropMap headerProps = Props.create();
        headerProps.setStyle(headerStyle);

        return new VNode(HtmlTag.TH, headerProps, dataTableColumn.getHeader());
    }

    private VNode renderBody(List<DataTableColumn> c) {
        PropMap props = tableProps(c);

        if(rowClickHandler != null) {
            props.onclick(event -> onBodyClick(event));
        }

        return H.div("datatable__body",
                H.table(props,
                    renderBodyHeaders(c),
                    renderBodyTable()));
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
        return H.tableHead(
                H.tableRow(columns.stream().map(this::renderBodyHeader))
        );
    }

    private VTree renderBodyHeader(DataTableColumn dataTableColumn) {
        Style headerStyle = new Style();
        headerStyle.set("width", dataTableColumn.getWidth() + "px");
        headerStyle.set("height", "0px");

        PropMap headerProps = Props.create();
        headerProps.setStyle(headerStyle);

        return new VNode(HtmlTag.TH, headerProps);
    }

    private VTree renderBodyTable() {
        Observable<RowRange> range = Observable.just(new RowRange());
        Observable<VTree> tableBody = rowRenderer.apply(range);

        return new ReactiveComponent(tableBody);
    }

    private class Component extends VComponent {

        private final VTree content;
        private ScrollListener eventListener;
        private Subscription selectionSubscription;

        private String previousSelection = "";

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

            selectionSubscription = selectedRow.subscribe(o -> updateSelection());
        }
        @Override
        protected void componentWillUnmount() {
            eventListener.removeListener();
            eventListener = null;
            selectionSubscription.unsubscribe();
            selectionSubscription = null;
        }

        private void updateSelection() {
            if(selectedRow.isLoaded()) {
                String newSelection = selectedRow.get();
                if (!newSelection.equals(previousSelection)) {
                    Element container = getContainer();
                    if (!previousSelection.isEmpty()) {
                        Element selectedRow = container.querySelector(".selected");
                        if (selectedRow != null) {
                            selectedRow.classList.remove("selected");
                        }
                    }
                    if (!newSelection.isEmpty()) {
                        Element selectedRow = container.querySelector("tr[data-row=\"" + newSelection + "\"]");
                        if(selectedRow != null) {
                            selectedRow.classList.add("selected");
                        }
                    }
                    previousSelection = newSelection;
                }
            }
        }
    }

    /**
     * Keeps the header in sync with the body's horizontal scrolling
     */
    private static class ScrollListener implements EventListener {
        private final Element tableBody;
        private final HTMLElement headerTable;

        public ScrollListener(Element tableBody, Element headerTable) {
            this.tableBody = tableBody;
            this.headerTable = (HTMLElement) headerTable;
        }

        @Override
        public void handleEvent(Event evt) {
            headerTable.style.left = (-tableBody.scrollLeft) + "px";
        }

        public void addListener() {
            tableBody.addEventListener("scroll", this);
        }

        public void removeListener() {
            tableBody.removeEventListener("scroll", this);
        }
    }
}
