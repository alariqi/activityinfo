package org.activityinfo.ui.client.base.container;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;

public class StaticHtml extends Widget {

    public StaticHtml(SafeHtml html) {
        DivElement div = Document.get().createDivElement();
        div.setInnerSafeHtml(html);
        assert hasSingleTopLevelElement(div) :
                "supplied html should have single root <element>:\n" + html.asString();

        setElement(div.getFirstChildElement());
    }

    @Override
    public XElement getElement() {
        return super.getElement().cast();
    }

    private static native boolean hasSingleTopLevelElement(DivElement div) /*-{
        if(!div.childElementCount) {
            return true;
        }
        return div.childElementCount == 1;
    }-*/;
}
