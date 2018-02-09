package chdc.frontend.client;

import chdc.frontend.client.theme.Banner;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

/**
 * Outer container for the application
 */
public class ChdcFrame implements IsWidget, AcceptsOneWidget {

    private BorderLayoutContainer container;

    public ChdcFrame() {
        this.container = new BorderLayoutContainer();
        this.container.setNorthWidget(new Banner(), new BorderLayoutContainer.BorderLayoutData(54));
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void setWidget(IsWidget w) {
        this.container.setCenterWidget(w);
    }
}
