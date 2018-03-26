package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class Frame implements AcceptsOneWidget, IsWidget {

    private final VerticalLayoutContainer container = new VerticalLayoutContainer();
    private Widget currentPageWidget;

    public Frame() {
        container.add(new Header(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
    }

    @Override
    public void setWidget(IsWidget w) {
        Widget newWidget = w.asWidget();
        if(newWidget != currentPageWidget) {
            container.remove(newWidget);
            container.add(newWidget, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
            currentPageWidget = newWidget;

            Scheduler.get().scheduleFinally(container::forceLayout);
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
