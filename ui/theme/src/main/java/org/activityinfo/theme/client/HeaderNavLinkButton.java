package org.activityinfo.theme.client;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;


public class HeaderNavLinkButton implements IsWidget {

    private HTML html;

    public HeaderNavLinkButton(Icon icon, SafeUri uri, String label) {
        html = new HTML(Templates.TEMPLATES.headerNavLinkButton(uri, label));
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
