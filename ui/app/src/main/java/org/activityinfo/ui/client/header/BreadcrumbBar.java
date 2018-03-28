package org.activityinfo.theme.client;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class BreadcrumbBar implements IsWidget {

    private final ToolBar toolBar;

    public BreadcrumbBar() {
        toolBar = new ToolBar();
    }

    public void clear() {
        toolBar.clear();
    }

    public void addLink(String label, SafeUri href) {
        maybeAddSeparator();
        toolBar.add(new HTML(Templates.TEMPLATES.breadcrumb(label, href)));
    }

    public void addLink(Icon icon, String label, SafeUri href) {
        maybeAddSeparator();
        toolBar.add(new HTML(Templates.TEMPLATES.breadcrumb(label, href)));
    }

    private void maybeAddSeparator() {
        if(toolBar.getWidgetCount() > 0) {
            toolBar.add(new HTML(Templates.TEMPLATES.breadcrumbSeparator()));
        }
    }

    @Override
    public Widget asWidget() {
        return toolBar;
    }

    public void addMenu(Icon icon, String label, Menu menu) {
        TextButton button = new TextButton(label);
        button.setMenu(menu);

        maybeAddSeparator();
        toolBar.add(button);
    }
}
