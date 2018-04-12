package org.activityinfo.dev.client;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

public class ComponentPanel implements IsWidget {

    private FlowLayoutContainer container = new FlowLayoutContainer();

    public ComponentPanel(String heading, IsWidget component) {
        this.container.addStyleName(DevBundle.RESOURCES.style().component());
        this.container.add(new HTML("<h2>" + SafeHtmlUtils.htmlEscape(heading) + "</h2>"));
        this.container.add(component);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
