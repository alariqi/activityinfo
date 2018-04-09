package org.activityinfo.theme.client.tablegrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;
import org.activityinfo.theme.client.ClassNames;

public class ColumnHeaderAppearance implements ColumnHeader.ColumnHeaderAppearance {

    public static class Styles implements ColumnHeader.ColumnHeaderStyles {

        @Override
        public String columnMoveBottom() {
            return ClassNames.TABLEGRID__COLUMN_MOVE_BOTTOM;
        }

        @Override
        public String columnMoveTop() {
            return ClassNames.TABLEGRID__COLUMN_MOVE_TOP;
        }

        @Override
        public String head() {
            return ClassNames.TABLEGRID__HEAD;
        }

        @Override
        public String headButton() {
            return ClassNames.TABLEGRID__HEAD_BUTTON;
        }

        @Override
        public String header() {
            return ClassNames.TABLEGRID__HEADER;
        }

        @Override
        public String headerInner() {
            return ClassNames.TABLEGRID__HEADER_INNER;
        }

        @Override
        public String headInner() {
            return ClassNames.TABLEGRID__HEAD_INNER;
        }

        @Override
        public String headMenuOpen() {
            return ClassNames.TABLEGRID__HEAD_MENU__OPEN;
        }

        @Override
        public String headOver() {
            return ClassNames.TABLEGRID__HEAD__OVER;
        }

        @Override
        public String headRow() {
            return ClassNames.TABLEGRID__HEAD_ROW;
        }

        @Override
        public String sortAsc() {
            return ClassNames.TABLEGRID__SORT__ASC;
        }

        @Override
        public String sortDesc() {
            return ClassNames.TABLEGRID__SORT__DESC;
        }

        @Override
        public String sortIcon() {
            return ClassNames.TABLEGRID__SORT_ICON;
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
        return "." + ClassNames.TABLEGRID__HEAD_INNER;
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
