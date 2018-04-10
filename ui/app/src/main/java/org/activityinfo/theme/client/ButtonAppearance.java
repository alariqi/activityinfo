package org.activityinfo.theme.client;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.core.client.dom.XElement;

public class ButtonAppearance<M> implements ButtonCell.ButtonCellAppearance<M> {

    @Override
    public void render(ButtonCell<M> cell, Cell.Context context, M value, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<button type=\"button\" class=\"button\">");
        if(value != null) {
            sb.appendEscaped(value.toString());
        }
        sb.appendHtmlConstant("</button>");
    }

    @Override
    public XElement getButtonElement(XElement parent) {
        return parent.getFirstChildElement().cast();
    }

    @Override
    public XElement getFocusElement(XElement parent) {
        return getButtonElement(parent);
    }

    @Override
    public void onFocus(XElement parent, boolean focused) {
    }

    @Override
    public void onOver(XElement parent, boolean over) {
    }

    @Override
    public void onPress(XElement parent, boolean pressed) {
    }

    @Override
    public void onToggle(XElement parent, boolean pressed) {
    }

}
