package org.activityinfo.theme.client.search;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.dom.XElement;

public class SearchBoxAppearance implements TriggerFieldCell.TriggerFieldAppearance {

    @Override
    public void render(SafeHtmlBuilder sb, String value, FieldCell.FieldAppearanceOptions options) {
        sb.append(SearchTemplates.TEMPLATES.searchBox());
    }

    @Override
    public XElement getInputElement(Element parent) {
        return parent.getElementsByTagName("input").getItem(0).cast();
    }

    @Override
    public boolean triggerIsOrHasChild(XElement parent, Element target) {
        return false;
    }

    @Override
    public void onResize(XElement parent, int width, int height, boolean hideTrigger) {
    }

    @Override
    public void onTriggerClick(XElement parent, boolean click) {
    }

    @Override
    public void onTriggerOver(XElement parent, boolean over) {
    }


    @Override
    public void setEditable(XElement parent, boolean editable) {
    }


    @Override
    public void onEmpty(Element parent, boolean empty) {
    }

    @Override
    public void onFocus(Element parent, boolean focus) {
    }

    @Override
    public void onValid(Element parent, boolean valid) {
    }

    @Override
    public void setReadOnly(Element parent, boolean readonly) {
    }
}
