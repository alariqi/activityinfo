package org.activityinfo.ui.client.base.button;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.core.client.dom.XElement;
import org.activityinfo.ui.client.Icon;

public class IconButtonAppearance implements ButtonCell.ButtonCellAppearance<String> {

    private Icon icon;
    private final IconButtonStyle style;

    public IconButtonAppearance(Icon icon, IconButtonStyle style) {
        this.icon = icon;
        this.style = style;
    }

    @Override
    public void render(ButtonCell<String> cell, Cell.Context context, String value, SafeHtmlBuilder sb) {
        sb.append(ButtonTemplates.TEMPLATES.iconButton(icon.href(), value,
                style == IconButtonStyle.PRIMARY ? "button button--primary" : ""));
    }

    @Override
    public XElement getButtonElement(XElement parent) {
        return parent.getFirstChildElement().cast();
    }

    @Override
    public XElement getFocusElement(XElement parent) {
        return parent.getFirstChildElement().cast();
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
