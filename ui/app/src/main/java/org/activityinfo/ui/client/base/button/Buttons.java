package org.activityinfo.ui.client.base.button;

import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

public class Buttons {

    private String buttonClasses = "button";
    private VTree iconNode;
    private String label;
    private EventHandler handler;

    public Buttons(String label) {
        this.label = label;
    }

    public static Buttons button(String label) {
        return new Buttons(label);
    }

    public Buttons primary() {
        buttonClasses += " button--primary";
        return this;
    }

    public Buttons icon(Icon icon) {
        iconNode = icon.tree();
        return this;
    }

    public Buttons onSelect(EventHandler handler) {
        this.handler = handler;
        return this;
    }

    public VTree build() {
        PropMap buttonProps = new PropMap();
        buttonProps.setClass(buttonClasses);
        if(handler != null) {
            buttonProps.onclick(handler);
        }

        List<VTree> children = new ArrayList<>();
        if(iconNode != null) {
            children.add(iconNode);
        }
        children.add(new VNode(HtmlTag.SPAN, PropMap.withClasses("button__label"), new VText(label)));

        return new VNode(HtmlTag.BUTTON, buttonProps, children);
    }
}
