package org.activityinfo.ui.client.base.button;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.button.ButtonGroup;

public class ButtonGroupAppearance implements ButtonGroup.ButtonGroupAppearance {
    @Override
    public void render(SafeHtmlBuilder sb) {
        sb.append(ButtonTemplates.TEMPLATES.buttonGroup());
    }

    @Override
    public void setHeading(XElement parent, SafeHtml html) {
        getHeaderElement(parent).setInnerSafeHtml(html);
    }

    @Override
    public XElement getHeaderElement(XElement parent) {
        return parent.selectNode(".buttongroup__header");
    }

    @Override
    public void onHideHeader(XElement parent, boolean hide) {
        getHeaderElement(parent).setVisible(!hide);
    }


    @Override
    public XElement getContentElem(XElement parent) {
        return parent.selectNode(".buttongroup__body");
    }

    @Override
    public int getFrameHeight(XElement parent) {
        return 2;
    }

    @Override
    public int getFrameWidth(XElement parent) {
        return 2;
    }
}