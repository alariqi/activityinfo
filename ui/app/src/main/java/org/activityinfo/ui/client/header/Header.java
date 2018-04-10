package org.activityinfo.ui.client.header;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.theme.client.CssLayoutContainer;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.ui.client.search.SearchBox;
import org.activityinfo.ui.client.store.FormStore;

public class Header implements IsWidget {

    private CssLayoutContainer container = new CssLayoutContainer("header");

    public Header(FormStore formStore) {
        container.addStyleName("header");
        container.add(new Logo());
        container.add(new SearchBox(formStore));

        CssLayoutContainer navContainer = new CssLayoutContainer("nav");
        navContainer.add(new NavButton(Icon.REPORTS, UriUtils.fromSafeConstant("#"), "Reports"));
        navContainer.add(new NavButton(Icon.TASKS, UriUtils.fromSafeConstant("#"), I18N.CONSTANTS.notifications()   ));
        navContainer.add(new NavButton(Icon.SETTINGS, UriUtils.fromSafeConstant("#"), I18N.CONSTANTS.settings()));

        container.add(navContainer);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
