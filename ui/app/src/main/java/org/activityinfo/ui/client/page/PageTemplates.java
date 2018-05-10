package org.activityinfo.ui.client.page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface PageTemplates extends XTemplates {

    PageTemplates TEMPLATES = GWT.create(PageTemplates.class);

    @XTemplate(source = "PageHeader.html")
    SafeHtml header();

    @XTemplate(source = "GenericAvatar.html")
    SafeHtml genericAvatar(String iconHref);

}
