package org.activityinfo.ui.client.header;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.search.SearchBox;
import org.activityinfo.ui.client.search.SearchResult;

import java.util.List;

public class Header implements IsWidget {

    private final NavButton notificationsLink;
    private final NavButton settingsLink;
    private final NavButton reportsLink;

    private final CssLayoutContainer container = new CssLayoutContainer("header");

    public Header(Observable<List<SearchResult>> resourceList) {
        container.addStyleName("header");
        container.add(new Logo());
        container.add(new SearchBox(resourceList));

        reportsLink = new NavButton(Icon.HEADER_REPORTS, UriUtils.fromSafeConstant("#"), "Reports");
        notificationsLink = new NavButton(Icon.HEADER_NOTIFICATION, UriUtils.fromSafeConstant("#"), I18N.CONSTANTS.notifications());
        settingsLink = new NavButton(Icon.HEADER_SETTINGS, UriUtils.fromSafeConstant("#"), I18N.CONSTANTS.settings());

        CssLayoutContainer navContainer = new CssLayoutContainer("nav");
        navContainer.add(reportsLink);
        navContainer.add(notificationsLink);
        navContainer.add(settingsLink);

        container.add(navContainer);
    }


    public void setSettingsActive(boolean active) {
        settingsLink.setActive(active);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
