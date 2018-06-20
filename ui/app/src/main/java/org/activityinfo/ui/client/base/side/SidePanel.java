package org.activityinfo.ui.client.base.side;

import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import static org.activityinfo.ui.vdom.shared.tree.PropMap.withClasses;

public class SidePanel {

    public enum Side {
        LEFT,
        RIGHT
    }

    private StatefulValue<Boolean> expanded = new StatefulValue<>(false);
    private VTree title = new VText("");
    private VTree content = new VNode(HtmlTag.DIV);
    private VTree header = new VText("");
    private Side side = Side.RIGHT;
    private boolean full = false;
    private String expandedWidth = null;

    public SidePanel expanded(StatefulValue<Boolean> expanded) {
        this.expanded = expanded;
        return this;
    }

    public SidePanel content(VTree tree) {
        this.content = tree;
        return this;
    }

    public SidePanel title(VTree tree) {
        this.title = tree;
        return this;
    }

    public SidePanel header(VTree tree) {
        this.header = tree;
        return this;
    }

    public SidePanel leftSide() {
        this.side = Side.LEFT;
        return this;
    }

    public SidePanel full() {
        return full(true);
    }

    public SidePanel full(boolean full) {
        this.full = full;
        return this;
    }

    public SidePanel expandedWidth(String width) {
        this.expandedWidth = width;
        return this;
    }

    /**
     * The label of the expand button visible when the panel is collapsed.
     */
    public SidePanel expandButtonLabel(String text) {
        return title(new VText(text));
    }

    public VTree build() {
        return new ReactiveComponent("sidepanel", expanded.transform(e -> {

            PropMap props = new PropMap();
            props.addClassName("sidepanel");
            switch (side) {
                case LEFT:
                    props.addClassName("sidepanel--left");
                    break;
                case RIGHT:
                    props.addClassName("sidepanel--right");
                    break;
            }

            if(!e) {
                props.addClassName("sidepanel--collapsed");
            } else if(full) {
                props.addClassName("sidepanel--full");
            } else {
                if(expandedWidth != null) {
                    Style style = new Style();
                    style.set("width", expandedWidth);

                    props.setStyle(style);
                }
            }

            if(e) {
                return new VNode(HtmlTag.DIV, props,
                        header(),
                        content());
            } else {
                return new VNode(HtmlTag.DIV, props, expandButton());
            }
        }));
    }



    private VNode expandButton() {
        return new VNode(HtmlTag.BUTTON, withClasses("sidepanel__expand").onclick(event -> {
            expanded.updateIfNotEqual(true);
        }), title, new VText(" ▲"));
    }

    private VNode collapseButton() {
        return new VNode(HtmlTag.BUTTON, withClasses("sidepanel__collapse").onclick(event -> {
            expanded.updateIfNotEqual(false);
        }), NonIdeal.svg(null, "#close_white"));
    }


    private VTree header() {
        return new VNode(HtmlTag.DIV, withClasses("sidepanel__header"),
                header,
                collapseButton());
    }

    private VNode content() {
        return new VNode(HtmlTag.DIV, withClasses("sidepanel__content"), this.content);
    }
}
