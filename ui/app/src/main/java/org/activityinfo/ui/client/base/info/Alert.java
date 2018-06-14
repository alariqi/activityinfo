package org.activityinfo.ui.client.base.info;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;

/**
 * Simple message drawing the user's attention to an error or a warning.
 */
public class Alert extends Widget {

    private XElement div;

    public enum Type {
        ERROR,
        WARNING
    }

    public Alert() {
        div = Document.get().createDivElement().cast();
        div.addClassName("alert");
        setElement(div.<Element>cast());
    }

    public Alert(Type type, String message) {
        this();
        setType(type);
        setMessage(message);
    }

    public void setMessage(String message) {
        div.setInnerText(message);
    }

    public void setType(Type type) {
        div.setClassName("alert--error", type == Type.ERROR);
        div.setClassName("alert--warning", type == Type.WARNING);
    }

    public void setVisible(boolean visible) {
        div.setVisible(visible);
    }
}
