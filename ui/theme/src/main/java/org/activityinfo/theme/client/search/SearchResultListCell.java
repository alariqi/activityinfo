package org.activityinfo.theme.client.search;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

class SearchResultListCell extends AbstractCell<SearchResult> {
    @Override
    public void render(Context context, SearchResult value, SafeHtmlBuilder sb) {
        sb.append(SearchTemplates.TEMPLATES.searchItem(value));
    }
}
