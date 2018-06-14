package org.activityinfo.ui.client.base.listtable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface ListTableTemplates extends XTemplates {

    ListTableTemplates TEMPLATES = GWT.create(ListTableTemplates.class);

    @XTemplate(source = "ListItem.html")
    SafeHtml listItem(ListItem item);

    @XTemplate(source = "EmptyList.html")
    SafeHtml emptyList();

}
