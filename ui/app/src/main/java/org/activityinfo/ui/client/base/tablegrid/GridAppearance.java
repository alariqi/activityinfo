package org.activityinfo.ui.client.base.tablegrid;

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
import org.activityinfo.ui.client.base.ClassNames;

public class GridAppearance implements GridView.GridAppearance {

    public interface GridTemplates extends XTemplates {
        @XTemplate(source = "TableGrid.html")
        SafeHtml render();
    }

    public static class Styles implements GridView.GridStyles {

        @Override
        public String cell() {
            return ClassNames.TABLEGRID__CELL;
        }

        @Override
        public String cellDirty() {
            return ClassNames.TABLEGRID__CELL__DIRTY;
        }

        @Override
        public String cellInner() {
            return ClassNames.TABLEGRID__CELL_INNER;

        }

        @Override
        public String noPadding() {
            return ClassNames.NOPADDING;
        }

        @Override
        public String columnLines() {
            return ClassNames.TABLEGRID__COLUMN_LINES;
        }

        @Override
        public String dataTable() {
            return ClassNames.TABLEGRID_DATA_TABLE;
        }

        @Override
        public String headerRow() {
            return ClassNames.TABLEGRID__HEADER_ROW;
        }

        @Override
        public String row() {
            return ClassNames.TABLEGRID__ROW;
        }

        @Override
        public String rowAlt() {
            return ClassNames.TABLEGRID__ROW__ALT;
        }

        @Override
        public String rowBody() {
            return ClassNames.TABLEGRID__ROW_BODY;
        }

        @Override
        public String rowDirty() {
            return ClassNames.TABLEGRID__ROW__DIRTY;
        }

        @Override
        public String rowHighlight() {
            return ClassNames.TABLEGRID__ROW__HIGHLIGHT;
        }

        @Override
        public String rowOver() {
            return ClassNames.TABLEGRID__ROW__OVER;
        }

        @Override
        public String rowWrap() {
            return ClassNames.TABLEGRID__ROW_WRAP;
        }

        @Override
        public String empty() {
            return ClassNames.TABLEGRID__EMPTY;
        }

        @Override
        public String footer() {
            return ClassNames.TABLEGRID__FOOTER;
        }

        @Override
        public String grid() {
            return ClassNames.TABLEGRID;
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
