package org.activityinfo.ui.client.page;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;

public class FullWidthPageContainer implements IsWidget {

    private final CssLayoutContainer container;
    private final PageHeader header;
    private final CssLayoutContainer body;

    public FullWidthPageContainer() {

        header = new PageHeader();

        body = new CssLayoutContainer();
        body.addStyleName("page__body");

        container = new CssLayoutContainer();
        container.addStyleName("page");
        container.addStyleName("page--fullwidth");
        container.add(header);
        container.add(body);
    }

    public PageHeader getHeader() {
        return header;
    }

    public void addBodyWidget(IsWidget widget) {
        body.add(widget);
    }

    /**
     * Adds a CSS class name to the {@code page__body__inner} div.
     */
    public void addBodyStyleName(String className) {
        body.addStyleName(className);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
