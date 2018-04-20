package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import org.activityinfo.ui.client.base.container.StaticHtml;

public class ConnectionStatus implements IsWidget {

    private final StaticHtml html;

    interface Templates extends XTemplates {

        @XTemplate(source = "ConnectionStatus.html")
        SafeHtml render();
    }

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    public ConnectionStatus() {
        html = new StaticHtml(TEMPLATES.render());
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
