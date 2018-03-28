package org.activityinfo.theme.client;


import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class NavListItemCell extends AbstractCell<NavListItem> {

    @Override
    public void render(Context context, NavListItem item, SafeHtmlBuilder sb) {
        sb.append(Templates.TEMPLATES.navListItem(item));
    }
}
