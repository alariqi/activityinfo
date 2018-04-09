package org.activityinfo.theme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;

public interface Templates extends XTemplates {

    Templates TEMPLATES = GWT.create(Templates.class);

    @XTemplate(source = "Breadcrumb.html")
    SafeHtml breadcrumb(String label, SafeUri href);

    @XTemplate(source = "BreadcrumbSeparator.html")
    SafeHtml breadcrumbSeparator();

    @XTemplate(source = "NavListItem.html")
    SafeHtml navListItem(NavListItem item);

    @XTemplate(source = "PageHeader.html")
    SafeHtml pageHeader(String heading, SafeUri settingsUri, String settingsLabel);
}
