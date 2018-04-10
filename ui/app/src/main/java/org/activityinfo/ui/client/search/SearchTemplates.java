package org.activityinfo.ui.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

interface SearchTemplates extends XTemplates {
    SearchTemplates TEMPLATES = GWT.create(SearchTemplates.class);

    @XTemplate(source = "SearchBox.html")
    SafeHtml searchBox();

    @XTemplate(source = "SearchList.html")
    SafeHtml searchList();

    @XTemplate(source = "SearchItem.html")
    SafeHtml searchItem(SearchResult result);

    @XTemplate("<strong>{database}</strong>")
    SafeHtml databaseLabel(String database);
}
