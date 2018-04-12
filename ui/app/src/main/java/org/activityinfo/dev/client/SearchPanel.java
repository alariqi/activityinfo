package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.search.SearchBox;

public class SearchPanel implements IsWidget {

    private final ComponentPanel panel;

    public SearchPanel() {


        panel = new ComponentPanel("Search", new SearchBox(SearchResults.createStore()));
    }

    @Override
    public Widget asWidget() {
        return panel.asWidget();
    }
}
