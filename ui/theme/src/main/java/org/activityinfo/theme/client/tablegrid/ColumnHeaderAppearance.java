package org.activityinfo.theme.client.tablegrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;

public class ColumnHeaderAppearance implements ColumnHeader.ColumnHeaderAppearance {

    public static class Styles implements ColumnHeader.ColumnHeaderStyles {

        @Override
        public String columnMoveBottom() {
            return "tablegrid__column-move-bottom";
        }

        @Override
        public String columnMoveTop() {
            return "tablegrid__column-move-top";
        }

        @Override
        public String head() {
            return "tablegrid__head";
        }

        @Override
        public String headButton() {
            return "tablegrid__head-button";
        }

        @Override
        public String header() {
            return "tablegrid__header";
        }

        @Override
        public String headerInner() {
            return "tablegrid__header-inner";
        }

        @Override
        public String headInner() {
            return "tablegrid__head-inner";
        }

        @Override
        public String headMenuOpen() {
            return "tablegrid__head-menu--open";
        }

        @Override
        public String headOver() {
            return "tablegrid__head--over";
        }

        @Override
        public String headRow() {
            return "tablegrid__head-row";
        }

        @Override
        public String sortAsc() {
            return "tablegrid__sort--asc";
        }

        @Override
        public String sortDesc() {
            return "tablegrid__sort--desc";
        }

        @Override
        public String sortIcon() {
            return "tablegrid__sort-icon";
        }

        @Override
        public boolean ensureInjected() {
            // These styles are bundled, no dom modification
            return false;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public String getName() {
            return "styles";
        }
    }


    public interface Templates extends XTemplates {
        @XTemplate(source = "ColumnHeader.html")
        SafeHtml render();
    }

    private static final Styles STYLES = new Styles();

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    @Override
    public ImageResource columnsIcon() {
        return BlankImageResource.BLANK16;
    }

    @Override
    public String columnsWrapSelector() {
        return ".tablegrid__column-wrap";
    }

    @Override
    public void render(SafeHtmlBuilder sb) {
        sb.append(TEMPLATES.render());
    }

    @Override
    public ImageResource sortAscendingIcon() {
        return BlankImageResource.BLANK16;
    }

    @Override
    public ImageResource sortDescendingIcon() {
        return BlankImageResource.BLANK16;
    }

    @Override
    public ColumnHeader.ColumnHeaderStyles styles() {
        return STYLES;
    }

    @Override
    public int getColumnMenuWidth() {
        return 16;
    }
}