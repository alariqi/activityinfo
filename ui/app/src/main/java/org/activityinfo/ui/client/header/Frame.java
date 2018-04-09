package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import org.activityinfo.ui.client.search.SearchResult;

public class Frame implements AcceptsOneWidget, IsWidget {

    private final VerticalLayoutContainer container = new VerticalLayoutContainer();
    private Widget currentPageWidget;

    public Frame() {
        container.add(new Header(new ListStore<>(SearchResult::getId)),
                new VerticalLayoutContainer.VerticalLayoutData(1, -1));
    }

    @Override
    public void setWidget(IsWidget w) {

        Widget newWidget;
        if(w == null) {
            newWidget = null;
        } else {
            newWidget = w.asWidget();
        }

        if(newWidget != currentPageWidget) {
            if(currentPageWidget != null) {
                container.remove(currentPageWidget);
            }
            if(newWidget != null) {
                container.add(newWidget, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
            }
            currentPageWidget = newWidget;

            Scheduler.get().scheduleFinally(container::forceLayout);
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
