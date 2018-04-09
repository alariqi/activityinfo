package org.activityinfo.theme.dev.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

public class IndexPage implements IsWidget {

    private final FlowLayoutContainer links;

    public IndexPage() {
        links = new FlowLayoutContainer();
        for (DevPage page : DevPage.values()) {
            links.add(new HTML("<a href=\"#" + page.name() + "\">" + page.name() + "</a>"));
        }
    }

    @Override
    public Widget asWidget() {
        return links;
    }
}
