package org.activityinfo.theme.client.tablegrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.grid.GridView;

public class GridAppearance implements GridView.GridAppearance {

    public interface GridTemplates extends XTemplates {
        @XTemplate(source = "TableGrid.html")
        SafeHtml render();
    }

    public static class Styles implements GridView.GridStyles {

        @Override
        public String cell() {
            return "tablegrid__cell";
        }

        @Override
        public String cellDirty() {
            return "tablegrid__cell--dirty";
        }

        @Override
        public String cellInner() {
            return "tablegrid__cell-inner";
        }

        @Override
        public String noPadding() {
            return "tablegrid--no-padding";
        }

        @Override
        public String columnLines() {
            return "tablegrid__column-lines";
        }

        @Override
        public String dataTable() {
            return "tablegrid__data-table";
        }

        @Override
        public String headerRow() {
            return "tablegrid__header-row";
        }

        @Override
        public String row() {
            return "tablegrid__row";
        }

        @Override
        public String rowAlt() {
            return "tablegrid__row--alt";
        }

        @Override
        public String rowBody() {
            return "tablegrid__row-body";
        }

        @Override
        public String rowDirty() {
            return "tablegrid__row--dirty";
        }

        @Override
        public String rowHighlight() {
            return "tablegrid__row--highlight";
        }

        @Override
        public String rowOver() {
            return "tablegrid__row--over";
        }

        @Override
        public String rowWrap() {
            return "tablegrid__row-wrap";
        }

        @Override
        public String empty() {
            return "tablegrid--empty";
        }

        @Override
        public String footer() {
            return "tablegrid__footer";
        }

        @Override
        public String grid() {
            return "tablegrid";
        }

        @Override
        public boolean ensureInjected() {
            return false;
        }

        @Override
        public String getText() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            return "styles";
        }
    }

    private static final GridTemplates TEMPLATES = GWT.create(GridTemplates.class);

    private static final Styles STYLES = new Styles();

    @Override
    public void render(SafeHtmlBuilder sb) {
        sb.append(TEMPLATES.render());
    }

    @Override
    public GridView.GridStyles styles() {
        return STYLES;
    }

    @Override
    public Element findRow(Element elem) {
        if (Element.is(elem)) {
            return elem.<XElement> cast().findParentElement("." + STYLES.row(), -1);
        }
        return null;
    }

    @Override
    public NodeList<Element> getRows(XElement parent) {
        return TableElement.as(parent.getFirstChildElement()).getTBodies().getItem(1).getRows().cast();
    }

    @Override
    public Element findCell(Element elem) {
        if (Element.is(elem)) {
            return elem.<XElement> cast().findParentElement("." + STYLES.cell(), -1);
        }
        return null;
    }

    @Override
    public void onRowOver(Element row, boolean over) {
    }

    @Override
    public void onRowHighlight(Element row, boolean highlight) {
        row.<XElement> cast().setClassName(STYLES.rowHighlight(), highlight);
    }

    @Override
    public void onRowSelect(Element row, boolean select) {
    }

    @Override
    public void onCellSelect(Element cell, boolean select) {
    }

    @Override
    public Element getRowBody(Element row) {
        return TableElement.as(row.getFirstChildElement().getFirstChildElement().getFirstChildElement()).getTBodies().getItem(
                1).getRows().getItem(1).getCells().getItem(0).getFirstChildElement();
    }

    @Override
    public SafeHtml renderEmptyContent(String emptyText) {
        return Util.isEmptyString(emptyText) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(emptyText);
    }
}
