package chdc.frontend.client.theme;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Container;

/**
 * A replacement for Sencha's {@link com.sencha.gxt.widget.core.client.container.FlowLayoutContainer} that
 * does <strong>not</strong> set overflow:hidden on its element!
 */
public class CssLayoutContainer extends Container implements InsertPanel.ForIsWidget {

    public CssLayoutContainer() {
        setElement(Document.get().createDivElement());
    }

    @Override
    public void insert(IsWidget w, int beforeIndex) {
        insert(asWidgetOrNull(w), beforeIndex);
    }

    @Override
    public void insert(Widget w, int beforeIndex) {
        super.insert(w, beforeIndex);
    }

}
