package org.activityinfo.theme.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class PageHeader implements IsWidget {

    private final HTML html;

    public PageHeader(String heading, SafeUri settingsUri, String settingsLabel) {
        html = new HTML(Templates.TEMPLATES.pageHeader(heading, settingsUri, settingsLabel));
    }

    public PageHeader(String heading) {
        this(heading, UriUtils.fromSafeConstant("#"), "");
    }

    public void setHeading(String heading) {
        Element div = html.getElement().getFirstChildElement();
        Element h1 = div.getFirstChildElement();
        Element span = h1.getFirstChildElement();
        span.setInnerText(heading);
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
