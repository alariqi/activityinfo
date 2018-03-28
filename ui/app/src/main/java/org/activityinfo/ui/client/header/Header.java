package org.activityinfo.ui.client.header;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.theme.client.HeaderLogo;
import org.activityinfo.theme.client.HeaderNavLinkButton;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.ui.client.PlaceLinks;
import org.activityinfo.ui.client.databases.DatabaseListPlace;

public class Header implements IsWidget {

    private final HorizontalLayoutContainer container = new HorizontalLayoutContainer();

    public Header() {
        container.add(new HeaderLogo(), new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1));

        container.add(new HeaderNavLinkButton(
                Icon.DATABASE,
                PlaceLinks.toUri(new DatabaseListPlace()),
                I18N.CONSTANTS.databases()));

        container.add(new HeaderNavLinkButton(Icon.REPORTS,
                UriUtils.fromSafeConstant("#"),
                I18N.CONSTANTS.reports()));

        container.add(new HeaderSearchBox(), new HorizontalLayoutContainer.HorizontalLayoutData(1,-1));

        container.add(new HeaderNavLinkButton(Icon.TASKS, UriUtils.fromSafeConstant("#"), "Tasks"));
        container.add(new HeaderNavLinkButton(Icon.SETTINGS, UriUtils.fromSafeConstant("#"), "Settings"));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
