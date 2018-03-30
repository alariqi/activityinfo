package org.activityinfo.theme.client;

import com.google.gwt.dom.client.Document;
import com.sencha.gxt.widget.core.client.container.Container;

public class CssLayoutContainer extends Container {

    public CssLayoutContainer(String tagName) {
        setElement(Document.get().createElement(tagName));
    }

}
