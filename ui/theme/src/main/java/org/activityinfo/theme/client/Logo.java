package org.activityinfo.theme.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * ActivityInfo Corner Logo
 */
public final class Logo implements IsWidget {

    private final StaticHtml html;

    public Logo() {
        html = new StaticHtml(Templates.TEMPLATES.logo());
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
