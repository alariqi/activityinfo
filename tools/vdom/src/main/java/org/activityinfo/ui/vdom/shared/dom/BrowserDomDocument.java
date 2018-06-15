package org.activityinfo.ui.vdom.shared.dom;

import com.google.gwt.dom.client.Document;
import org.activityinfo.ui.vdom.shared.tree.Tag;

public class BrowserDomDocument implements DomDocument {

    private final Document document;

    public BrowserDomDocument() {
        this.document = Document.get();
    }

    public BrowserDomDocument(Document document) {
        this.document = document;
    }


    @Override
    public DomElement createElement(Tag tagName) {
        return document.createElement(tagName.name()).<BrowserDomElement>cast();
    }

    @Override
    public DomElement createElementNS(Tag tagName, String namespace) {
        return doCreateElementNS(tagName.name().toLowerCase(), namespace);
    }

    private static native DomElement doCreateElementNS(String name, String namespace) /*-{
        return $wnd.document.createElementNS(namespace, name);
    }-*/;

    @Override
    public DomText createTextNode(String text) {
        return document.createTextNode(text).<BrowserTextNode>cast();
    }
}
