package org.activityinfo.ui.client.base.button;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.Icon;

/**
 * A hyperlink that is styled as a button.
 *
 * <p>This button does not support any event handlers, it is meant to
 * render a simple html link.</p>
 */
public class IconLinkButton extends Widget {

    public IconLinkButton(Icon icon, SafeUri uri, String label) {
        AnchorElement anchorElement = Document.get().createAnchorElement();
        anchorElement.setClassName("button");
        anchorElement.setInnerSafeHtml(ButtonTemplates.TEMPLATES.iconLinkButton(icon.href(), label));
        anchorElement.setHref(uri);
        setElement(anchorElement);
    }
}
