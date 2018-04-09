package org.activityinfo.ui.client.header;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import org.activityinfo.theme.client.CssLayoutContainer;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.ui.client.search.SearchBox;
import org.activityinfo.ui.client.search.SearchResult;

public class Header implements IsWidget {

    private CssLayoutContainer container = new CssLayoutContainer("header");

    public Header(ListStore<SearchResult> store) {
        container.addStyleName("header");
        container.add(new Logo());
        container.add(new SearchBox(store));

        CssLayoutContainer navContainer = new CssLayoutContainer("nav");
        navContainer.add(new NavButton(Icon.REPORTS, UriUtils.fromSafeConstant("#"), "Reports"));
        navContainer.add(new NavButton(Icon.TASKS, UriUtils.fromSafeConstant("#"), "Tasks"));
        navContainer.add(new NavButton(Icon.SETTINGS, UriUtils.fromSafeConstant("#"), "Settings"));

        container.add(navContainer);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
