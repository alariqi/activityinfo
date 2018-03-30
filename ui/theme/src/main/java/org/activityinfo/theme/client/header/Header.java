package org.activityinfo.theme.client.header;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import org.activityinfo.theme.client.HeaderLogo;
import org.activityinfo.theme.client.HeaderNavLinkButton;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.search.SearchBox;
import org.activityinfo.theme.client.search.SearchResult;

public class Header implements IsWidget {

    private FlowLayoutContainer container = new FlowLayoutContainer();

    public Header(ListStore<SearchResult> store) {
        container.addStyleName("header");
        container.add(new HeaderLogo());
        container.add(new SearchBox(store));
        container.add(new HeaderNavLinkButton(Icon.REPORTS, UriUtils.fromSafeConstant("#"), "My reports"));
        container.add(new HeaderNavLinkButton(Icon.TASKS, UriUtils.fromSafeConstant("#"), "Tasks"));
        container.add(new HeaderNavLinkButton(Icon.SETTINGS, UriUtils.fromSafeConstant("#"), "Settings"));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
