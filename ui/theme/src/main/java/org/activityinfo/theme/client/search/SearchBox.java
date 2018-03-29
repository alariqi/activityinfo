package org.activityinfo.theme.client.search;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class SearchBox implements IsWidget {

    private final ComboBox<SearchResult> comboBox;

    public SearchBox(ListStore<SearchResult> store) {
        comboBox = new ComboBox<>(new SearchComboBoxCell(store));
    }

    @Override
    public Widget asWidget() {
        return comboBox;
    }
}
