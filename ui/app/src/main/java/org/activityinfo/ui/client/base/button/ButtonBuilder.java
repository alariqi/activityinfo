package org.activityinfo.ui.client.base.button;

import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

public class ButtonBuilder {

    private Tag buttonTag = HtmlTag.BUTTON;
    private VTree iconNode;
    private String label;
    private PropMap buttonProps = PropMap.withClasses("button");

    public ButtonBuilder(String label) {
        this.label = label;
    }

    /**
     * Style this button as a primary action, with a green background.
     */
    public ButtonBuilder primary() {
        buttonProps.addClassName("button--primary");
        return this;
    }

    /**
     * Style this button as a block element, that takes the full width of its container.
     */
    public ButtonBuilder block() {
        buttonProps.addClassName("button--block");
        return this;
    }

    /**
     * Add an icon to this button.
     */
    public ButtonBuilder icon(Icon icon) {
        iconNode = icon.tree();
        return this;
    }

    /**
     * Create a link that is styled as a button with the given {@code href}
     */
    public ButtonBuilder link(SafeUri href) {
        this.buttonTag = HtmlTag.A;
        this.buttonProps.href(href);
        return this;
    }

    /**
     * Add an event handler for this button being clicked, tapped, or otherwise selected.
     */
    public ButtonBuilder onSelect(EventHandler handler) {
        this.buttonProps.onclick(handler);
        return this;
    }

    public VTree build() {
        List<VTree> children = new ArrayList<>();
        if(iconNode != null) {
            children.add(iconNode);
        }
        children.add(new VNode(HtmlTag.SPAN, PropMap.withClasses("button__label"), new VText(label)));

        return new VNode(buttonTag, buttonProps, children);
    }
}
