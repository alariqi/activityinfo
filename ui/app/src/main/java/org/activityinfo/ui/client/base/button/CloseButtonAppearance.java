package org.activityinfo.ui.client.base.button;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.core.client.dom.XElement;

public class CloseButtonAppearance implements ButtonCell.ButtonCellAppearance<String> {
    private final String classNames;

    public CloseButtonAppearance() {
        this("button");
    }

    public CloseButtonAppearance(String classNames) {
        this.classNames = classNames;
    }

    @Override
    public void render(ButtonCell<String> cell, Cell.Context context, String value, SafeHtmlBuilder sb) {
        sb.append(ButtonTemplates.TEMPLATES.closeButton());
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
