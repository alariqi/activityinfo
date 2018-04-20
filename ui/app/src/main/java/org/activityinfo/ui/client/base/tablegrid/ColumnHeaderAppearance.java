package org.activityinfo.ui.client.base.tablegrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;

public class ColumnHeaderAppearance implements ColumnHeader.ColumnHeaderAppearance {

    public static final String TABLEGRID__COLUMN_MOVE_BOTTOM = "columnheader__column-move-bottom";
    public static final String TABLEGRID__COLUMN_MOVE_TOP = "columnheader__column-move-top";
    public static final String TABLEGRID__HEAD = "columnheader__head";
    public static final String TABLEGRID__HEAD__OVER = "columnheader__head--over";
    public static final String TABLEGRID__HEAD_BUTTON = "columnheader__head-button";
    public static final String TABLEGRID__HEAD_INNER = "columnheader__head-inner";
    public static final String TABLEGRID__HEAD_MENU__OPEN = "columnheader__head-menu--open";
    public static final String TABLEGRID__HEAD_ROW = "columnheader__head-row";
    public static final String TABLEGRID__HEADER = "columnheader__header";
    public static final String TABLEGRID__HEADER_INNER = "columnheader__header-inner";
    public static final String TABLEGRID__SORT__ASC = "columnheader__sort--asc";
    public static final String TABLEGRID__SORT__DESC = "columnheader__sort--desc";
    public static final String TABLEGRID__SORT_ICON = "columnheader__sort-icon";
    
    public static class Styles implements ColumnHeader.ColumnHeaderStyles {

        @Override
        public String columnMoveBottom() {
            return TABLEGRID__COLUMN_MOVE_BOTTOM;
        }

        @Override
        public String columnMoveTop() {
            return TABLEGRID__COLUMN_MOVE_TOP;
        }

        @Override
        public String head() {
            return TABLEGRID__HEAD;
        }

        @Override
        public String headButton() {
            return TABLEGRID__HEAD_BUTTON;
        }

        @Override
        public String header() {
            return TABLEGRID__HEADER;
        }

        @Override
        public String headerInner() {
            return TABLEGRID__HEADER_INNER;
        }

        @Override
        public String headInner() {
            return TABLEGRID__HEAD_INNER;
        }

        @Override
        public String headMenuOpen() {
            return TABLEGRID__HEAD_MENU__OPEN;
        }

        @Override
        public String headOver() {
            return TABLEGRID__HEAD__OVER;
        }

        @Override
        public String headRow() {
            return TABLEGRID__HEAD_ROW;
        }

        @Override
        public String sortAsc() {
            return TABLEGRID__SORT__ASC;
        }

        @Override
        public String sortDesc() {
            return TABLEGRID__SORT__DESC;
        }

        @Override
        public String sortIcon() {
            return TABLEGRID__SORT_ICON;
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
        return "." + TABLEGRID__HEAD_INNER;
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
