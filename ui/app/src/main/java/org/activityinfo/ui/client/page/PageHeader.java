package org.activityinfo.ui.client.page;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

public class PageHeader implements IsWidget {

    private HtmlLayoutContainer container;

    public PageHeader() {
        this.container = new HtmlLayoutContainer(PageTemplates.TEMPLATES.header());
    }

    public void setHeading(String text) {
        this.container.getElement().selectNode("h1").setInnerText(text);
    }

    public void setAvatar(Avatar avatar) {
        XElement avatarDiv = this.container.getElement().selectNode(".page__header__avatar");
        avatarDiv.setInnerSafeHtml(avatar.render());
        avatarDiv.removeAttribute("style");
    }

    public void addAction(IsWidget widget) {
        this.container.getElement().selectNode(".page__header__actions").setVisible(true);
        this.container.add(widget, new HtmlData(".page__header__actions"));
    }

    @Override
    public Widget asWidget() {
        return container;
    }

}
