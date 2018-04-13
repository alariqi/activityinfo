package org.activityinfo.ui.client.databases;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface DatabaseTemplates extends XTemplates {

    DatabaseTemplates TEMPLATES = GWT.create(DatabaseTemplates.class);

    @XTemplate(source = "PageHeader.html")
    SafeHtml pageHeader(String databases);

    @XTemplate(source = "ListItem.html")
    SafeHtml listItem(ListItem item);
}