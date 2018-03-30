package org.activityinfo.theme.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * ActivityInfo Corner Logo
 */
public final class HeaderLogo implements IsWidget {

    private final StaticHtml html;

    public HeaderLogo() {
        html = new StaticHtml(Templates.TEMPLATES.headerLogo());
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
