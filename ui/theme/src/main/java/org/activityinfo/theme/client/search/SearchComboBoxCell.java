package org.activityinfo.theme.client.search;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;

class SearchComboBoxCell extends ComboBoxCell<SearchResult> {

    SearchComboBoxCell(ListStore<SearchResult> store) {
        super(store, m -> m.getLabel(),
                new ListView<>(store, new IdentityValueProvider<>(), new SearchListAppearance()),
                new SearchBoxAppearance());
    }

    @Override
    protected boolean itemMatchesQuery(SearchResult item, String query) {
        return item.getLabel().toLowerCase().contains(query.toLowerCase());
    }
}
