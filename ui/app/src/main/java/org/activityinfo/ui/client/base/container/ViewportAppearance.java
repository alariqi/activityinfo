package org.activityinfo.ui.client.base.container;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class ViewportAppearance implements Viewport.ViewportAppearance {
    @Override
    public void render(SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<div class=\"viewport\"></div>");
    }
}
