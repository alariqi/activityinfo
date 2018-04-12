package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header;

public class DatabasesMockup implements IsWidget {

    private FlowLayoutContainer container;

    public DatabasesMockup() {
        this.container = new FlowLayoutContainer();
        this.container.add(new Header(SearchResults.createStore()));
        this.container.add(new ConnectionStatus());
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
