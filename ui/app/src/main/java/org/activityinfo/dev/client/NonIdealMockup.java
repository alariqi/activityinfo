package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header;

public class NonIdealMockup implements IsWidget {

    private CssLayoutContainer container;

    public NonIdealMockup(IsWidget page) {
        this.container = new CssLayoutContainer();
        this.container.add(new Header(SearchResults.getResourceList()));
        this.container.add(new ConnectionStatus());
        this.container.add(page);

    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
