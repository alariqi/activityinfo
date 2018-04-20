package org.activityinfo.ui.client.base.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

public class TabPanelAppearance implements TabPanel.TabPanelAppearance {

    private static final String ITEM_SELECTOR = "li";


    public interface ItemTemplate extends XTemplates {
        @XTemplate(source = "TabItem.html")
        SafeHtml item(TabItemConfig config);

        @XTemplate(source = "Tab.html")
        SafeHtml panel();
    }

    private static final ItemTemplate TEMPLATES = GWT.create(ItemTemplate.class);

    @Override
    public void createScrollers(XElement parent) {
        int h = getStripWrap(parent).getOffsetHeight();
        SafeHtml html = SafeHtmlUtils.fromTrustedString("<div class='tab__scroller-left'></div>");
        XElement scrollLeft = getBar(parent).insertFirst(html);
        scrollLeft.setId(XDOM.getUniqueId());
        scrollLeft.setHeight(h);

        html = SafeHtmlUtils.fromTrustedString("<div class='tab__scroller-right'></div>");
        XElement scrollRight = getBar(parent).insertFirst(html);
        scrollRight.setId(XDOM.getUniqueId());
        scrollRight.setHeight(h);
    }

    @Override
    public XElement getBar(XElement parent) {
        return parent.getFirstChildElement().cast();
    }

    @Override
    public XElement getBody(XElement parent) {
        return parent.selectNode(".tab__body");
    }

    @Override
    public String getItemSelector() {
        return ITEM_SELECTOR;
    }

    @Override
    public XElement getScrollLeft(XElement parent) {
        return getBar(parent).selectNode(".tab__scroller-left");
    }

    @Override
    public XElement getScrollRight(XElement parent) {
        return getBar(parent).selectNode(".tab__scroller-right");
    }

    public XElement getStrip(XElement parent) {
        return getBar(parent).selectNode(".tab__strip");
    }

    @Override
    public XElement getStripEdge(XElement parent) {
        return getBar(parent).selectNode(".tab__strip-edge");
    }

    @Override
    public XElement getStripWrap(XElement parent) {
        return getBar(parent).selectNode(".tab__strip-wrap");
    }

    @Override
    public void insert(XElement parent, TabItemConfig config, int index) {
        XElement item = XDOM.create(TEMPLATES.item(config));
        item.setClassName("tab__item--disabled", !config.isEnabled());

        getStrip(parent).insertChild(item, index);

        if (config.getIcon() != null) {
            setItemIcon(item, config.getIcon());
        }

        if (config.isClosable()) {
            item.addClassName("tab__item--closeable");
        }
    }

    @Override
    public boolean isClose(XElement target) {
        return target.is("." + "tab__strip-close");
    }

    @Override
    public void onDeselect(Element item) {
        item.removeClassName("tab__item--active");
    }

    @Override
    public void onMouseOut(XElement parent, XElement target) {
    }

    @Override
    public void onMouseOver(XElement parent, XElement target) {
    }

    @Override
    public void onScrolling(XElement parent, boolean scrolling) {
        parent.selectNode(".tab__bar").setClassName(".tab__bar--scrolling", scrolling);
    }

    @Override
    public void onSelect(Element item) {
        item.addClassName("tab__item--active");
    }

    @Override
    public void render(SafeHtmlBuilder builder) {
        builder.append(TEMPLATES.panel());
    }

    @Override
    public void setItemWidth(XElement element, int width) {
        element.setWidth(width, true);
    }

    @Override
    public void updateItem(XElement item, TabItemConfig config) {
        XElement contentEl = item.selectNode(".tab__strip-text");
        contentEl.setInnerSafeHtml(config.getContent());

        setItemIcon(item, config.getIcon());

        item.setClassName(".tab__item--disabled", !config.isEnabled());
        item.setClassName(".tab__item--closable", config.isClosable());
    }

    @Override
    public void updateScrollButtons(XElement parent) {
        int pos = getScrollPos(parent);
        getScrollLeft(parent).setClassName("tab__scroller-left--disabled", pos == 0);
        getScrollRight(parent).setClassName("tab__scroller-right--disabled",
                pos >= (getScrollWidth(parent) - getScrollArea(parent) - 2));
    }

    protected Element findItem(Element target) {
        return target.<XElement> cast().findParentElement(ITEM_SELECTOR, -1);
    }

    protected void setItemIcon(XElement item, ImageResource icon) {
//        XElement node = item.selectNode("." + "tab__icon");
//        if (node != null) {
//            node.removeFromParent();
//        }
//        if (icon != null) {
//            Element e = IconHelper.getElement(icon);
//            e.setClassName(style.tabImage());
//            item.appendChild(e);
//        }
//        item.setClassName("tab__item--with-icon", icon != null);
    }

    private int getScrollPos(XElement parent) {
        return getStripWrap(parent).getScrollLeft();
    }

    private int getScrollArea(XElement parent) {
        return Math.max(0, getStripWrap(parent).getClientWidth());
    }

    private int getScrollWidth(XElement parent) {
        return getStripEdge(parent).getOffsetsTo(getStripWrap(parent)).getX() + getScrollPos(parent);
    }
}
