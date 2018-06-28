package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import org.activityinfo.ui.client.base.container.StaticHtml;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class ConnectionStatus implements IsWidget {

    private final StaticHtml html;

    interface Templates extends XTemplates {

        @XTemplate(source = "ConnectionStatus.html")
        SafeHtml render();
    }

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    public ConnectionStatus() {
        html = new StaticHtml(TEMPLATES.render());
    }

    @Override
    public Widget asWidget() {
        return html;
    }

    public static VTree render() {
        return H.div("connectionstatus",
                H.div("connectionstatus__inner", new VNode(HtmlTag.DIV)));
    }

}
