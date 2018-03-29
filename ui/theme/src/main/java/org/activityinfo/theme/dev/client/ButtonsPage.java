package org.activityinfo.theme.dev.client;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import org.activityinfo.theme.client.HeaderLogo;
import org.activityinfo.theme.client.HeaderNavLinkButton;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.ListToolBarButton;

public class ButtonsPage implements IsWidget {

    private FlowLayoutContainer container = new FlowLayoutContainer();

    public ButtonsPage() {
        container.add(new ComponentPanel("HeaderLogo", new HeaderLogo()));
        container.add(new ComponentPanel("HeaderNavLinkButton",
                new HeaderNavLinkButton(Icon.DATABASE, UriUtils.fromSafeConstant("#"), "Databases")));
        container.add(new ComponentPanel("ListToolBarButton",
                new ListToolBarButton(Icon.ADD, "Click me")));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
