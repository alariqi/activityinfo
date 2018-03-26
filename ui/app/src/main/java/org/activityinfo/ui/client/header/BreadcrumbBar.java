package org.activityinfo.ui.client.header;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class BreadcrumbBar implements IsWidget {

    private final ToolBar toolBar;

    public BreadcrumbBar() {
        toolBar = new ToolBar();
    }

    @Override
    public Widget asWidget() {
        return toolBar;
    }
}
