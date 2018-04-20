
package org.activityinfo.ui.client.base.menu;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class Css3MenuItemAppearance implements MenuItem.MenuItemAppearance {

    @Override
    public void render(SafeHtmlBuilder result) {
        result.append(MenuTemplates.TEMPLATES.menuItem());
    }

    @Override
    public void setIcon(XElement parent, ImageResource icon) {

    }

    @Override
    public void setHtml(XElement parent, SafeHtml html) {
        XElement span = parent.selectNode("span");
        span.setInnerSafeHtml(html);
    }

    @Override
    public void setWidget(XElement parent, Widget widget) {
        throw new UnsupportedOperationException("TODO: setWidget");
    }

    @Override
    public void onAddSubMenu(XElement parent) {
        throw new UnsupportedOperationException("TODO: onAddSubMenu");
    }

    @Override
    public void onRemoveSubMenu(XElement parent) {
        throw new UnsupportedOperationException("TODO: onRemoveSubMenu");
    }

    @Override
    public void onActivate(XElement parent) {
        parent.addClassName("x-view-highlightrow");
    }

    @Override
    public void onDeactivate(XElement parent) {
        parent.removeClassName("x-view-highlightrow");
    }

}
