package org.activityinfo.ui.client.nonideal;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.container.StaticHtml;

public class PermissionDeniedPage implements IsWidget {

    private final StaticHtml html;

    public PermissionDeniedPage() {
        html = new StaticHtml(NonIdealTemplates.TEMPLATES.notFound());
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
