package org.activityinfo.ui.client.base.toolbar;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.button.MenuButton;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;

/**
 * A container for toolbar buttons.
 *
 * <p>This widget uses CSS flexbox for layout and should be used in place of
 * {@link com.sencha.gxt.widget.core.client.toolbar.ToolBar}</p>
 */
public class Toolbar implements IsWidget {

    private CssLayoutContainer container;
    private CssLayoutContainer actions;

    public Toolbar() {
        actions = new CssLayoutContainer();
        actions.addStyleName("toolbar__actions");

        container = new CssLayoutContainer();
        container.addStyleName("toolbar");
        container.add(actions);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    public void addAction(IsWidget widget) {
        actions.add(widget);
    }

    public void addSort(MenuButton menuButton) {
        container.add(menuButton);
    }
}
