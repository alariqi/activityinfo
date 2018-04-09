package org.activityinfo.theme.client.search;

import com.google.gwt.cell.client.ValueUpdater;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;

class SearchComboBoxCell extends ComboBoxCell<SearchResult> {

    SearchComboBoxCell(ListStore<SearchResult> store) {
        super(store, m -> m.getLabel(),
                createListView(store),
                new SearchBoxAppearance());
    }

    private static ListView<SearchResult, SearchResult> createListView(ListStore<SearchResult> store) {
        ListView<SearchResult, SearchResult> listView = new ListView<>(store,
                new IdentityValueProvider<>(),
                new SearchListAppearance());
        listView.setCell(new SearchResultListCell());
        return listView;
    }

    @Override
    protected boolean itemMatchesQuery(SearchResult item, String query) {
        return item.getLabel().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void expand(Context context, XElement parent, ValueUpdater<SearchResult> updater, SearchResult value) {
        super.expand(context, parent, updater, value);
        parent.addClassName("search--expanded");
    }

    @Override
    public void collapse(Context context, XElement parent) {
        super.collapse(context, parent);
        parent.removeClassName("search--expanded");
    }
}
