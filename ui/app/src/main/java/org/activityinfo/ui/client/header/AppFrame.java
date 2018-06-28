package org.activityinfo.ui.client.header;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.store.FormStore;

/**
 * The outer container for the application.
 */
public class AppFrame implements AcceptsOneWidget, IsWidget {

    private final CssLayoutContainer container;

    private Widget currentPageWidget;

    public AppFrame(FormStore formStore) {
        container = new CssLayoutContainer();
        container.add(new ConnectionStatus());
        container.addStyleName("appframe");
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
                container.add(newWidget);
            }
            currentPageWidget = newWidget;
            container.setStyleName("appframe--fixed", w instanceof HasFixedHeight);
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
