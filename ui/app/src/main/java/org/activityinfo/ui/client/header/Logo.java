package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import org.activityinfo.ui.client.base.container.StaticHtml;

/**
 * ActivityInfo Corner Logo
 */
public final class Logo implements IsWidget {


    interface Templates extends XTemplates {

        @XTemplate(source = "Logo.html")
        SafeHtml logo();
    }

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    private final StaticHtml html;


    public Logo() {
        html = new StaticHtml(TEMPLATES.logo());
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
