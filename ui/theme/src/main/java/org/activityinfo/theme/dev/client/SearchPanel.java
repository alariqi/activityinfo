package org.activityinfo.theme.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import org.activityinfo.theme.client.search.SearchBox;
import org.activityinfo.theme.client.search.SearchResult;

public class SearchPanel implements IsWidget {

    private final ComponentPanel panel;

    public SearchPanel() {
        ListStore<SearchResult> store = new ListStore<>(SearchResult::getId);
        store.addAll(SearchResults.LIST);

        panel = new ComponentPanel("Search", new SearchBox(store));

    }

    @Override
    public Widget asWidget() {
        return panel.asWidget();
    }
}
