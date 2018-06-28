package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.container.StaticHtml;

/**
 * ActivityInfo Corner Logo
 */
public final class Logo implements IsWidget {


    private static final LogoTemplate TEMPLATES = GWT.create(LogoTemplate.class);

    private final StaticHtml html;


    public Logo() {
        html = new StaticHtml(TEMPLATES.logo());
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
