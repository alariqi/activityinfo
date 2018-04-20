package org.activityinfo.ui.client.nonideal;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.container.StaticHtml;

public class LoadingPage implements IsWidget {

    private final StaticHtml html;

    public LoadingPage() {
        html = new StaticHtml(NonIdealTemplates.TEMPLATES.loading());
    }

    @Override
    public Widget asWidget() {
        return html;
    }
}
