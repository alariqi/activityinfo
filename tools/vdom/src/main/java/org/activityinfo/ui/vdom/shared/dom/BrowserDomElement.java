package org.activityinfo.ui.vdom.shared.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;

public class BrowserDomElement extends BrowserDomNode implements DomElement {

    protected BrowserDomElement() {}

    @Override
    public final void removeAttribute(String attrName) {
        asElement().removeAttribute(attrName);
    }

    @Override
    public final void setPropertyString(String propName, String propValue) {
        asElement().setPropertyString(propName, propValue);
    }

    @Override
    public final void clearStyleProperty(String name) {
        asElement().getStyle().clearProperty(name);
    }

    @Override
    public final void setAttribute(String key, String value) {
        asElement().setAttribute(key, value);
    }

    private Element asElement() {
        return this.cast();
    }

    @Override
    public final void setStyleProperty(String key, String value) {
        asElement().getStyle().setProperty(key, value);
    }

    @Override
    public final int getChildCount() {
        return asElement().getChildCount();
    }

    @Override
    public final String getTagName() {
        return asElement().getTagName();
    }

    public static BrowserDomElement cast(Element element) {
        return element.cast();
    }

    public static Element cast(DomElement element) {
        return ((BrowserDomElement)element).cast();
    }

    @Override // must be called only when mounted
    public final String getInputValue() {
        InputElement input = this.cast();
        return input.getValue();
    }

    @Override
    public final void setInputValue(String value) {
        InputElement input = this.cast();
        input.setValue(value);
    }
}
