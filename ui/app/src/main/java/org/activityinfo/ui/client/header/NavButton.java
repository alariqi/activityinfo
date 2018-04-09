package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.StaticHtml;


public class NavButton implements IsWidget {

    interface Templates extends XTemplates {
        @XTemplate(source = "NavButton.html")
        SafeHtml render(SafeUri uri, String label);
    }

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    private StaticHtml html;

    public NavButton(Icon icon, SafeUri uri, String label) {
        html = new StaticHtml(TEMPLATES.render(uri, label));
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
