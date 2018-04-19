package org.activityinfo.ui.client.databases;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import org.activityinfo.i18n.shared.UiConstants;

public interface DatabaseTemplates extends XTemplates {

    DatabaseTemplates TEMPLATES = GWT.create(DatabaseTemplates.class);

    @XTemplate(source = "DatabaseListPage.html")
    SafeHtml page(UiConstants i18n);

    @XTemplate(source = "ListItem.html")
    SafeHtml listItem(ListItem item);

    @XTemplate(source = "EmptyList.html")
    SafeHtml emptyList();
}
