package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header;

public class TableMockup implements IsWidget {


    private final VerticalLayoutContainer container;

    public TableMockup() {
        this.container = new VerticalLayoutContainer();
        this.container.add(new Header(SearchResults.createStore()), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        this.container.add(new ConnectionStatus(), new VerticalLayoutContainer.VerticalLayoutData(1, 16));
        this.container.add(new PageHeaderDummy(), new VerticalLayoutContainer.VerticalLayoutData(1, 150));
        this.container.add(new GridPanel(), new VerticalLayoutContainer.VerticalLayoutData(1, 1));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
