package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class PageHeaderDummy implements IsWidget {
    private final HTML html = new HTML();

    public PageHeaderDummy() {
        html.getElement().getStyle().setBackgroundColor("white");
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
