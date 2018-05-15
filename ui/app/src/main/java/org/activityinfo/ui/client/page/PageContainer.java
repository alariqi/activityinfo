package org.activityinfo.ui.client.page;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;

public class PageContainer implements IsWidget {

    private final CssLayoutContainer container;
    private final PageHeader header;
    private final CssLayoutContainer body;
    private final CssLayoutContainer bodyInner;

    public PageContainer() {

        header = new PageHeader();

        bodyInner = new CssLayoutContainer();
        bodyInner.addStyleName("page__body__inner");

        body = new CssLayoutContainer();
        body.addStyleName("page__body");
        body.add(bodyInner);

        container = new CssLayoutContainer();
        container.addStyleName("page");
        container.addStyleName("page--padded");
        container.add(header);
        container.add(body);
    }

    public PageHeader getHeader() {
        return header;
    }

    public void addBodyWidget(IsWidget widget) {
        bodyInner.add(widget);
    }

    /**
     * Adds a CSS class name to the {@code page__body__inner} div.
     */
    public void addBodyStyleName(String className) {
        bodyInner.addStyleName(className);
    }

    public void setVisible(boolean visible) {
        container.setVisible(visible);
    }

    public void setBreadcrumbs(Breadcrumb... breadcrumbs) {
        header.setBreadcrumbs(breadcrumbs);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
