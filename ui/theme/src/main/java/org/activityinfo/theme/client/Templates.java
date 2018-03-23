package org.activityinfo.theme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;

public interface Templates extends XTemplates {

    Templates TEMPLATES = GWT.create(Templates.class);

    @XTemplate(source = "HeaderLogo.html")
    SafeHtml headerLogo();

    @XTemplate(source = "HeaderNavLinkButton.html")
    SafeHtml headerNavLinkButton(SafeUri uri, String label);

    @XTemplate(source = "PageHeader.html")
    SafeHtml pageHeader(String heading, SafeUri settingsUri, String settingsLabel);
}
