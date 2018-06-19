package org.activityinfo.ui.client.base.side;

import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import static org.activityinfo.ui.vdom.shared.tree.PropMap.withClasses;

public class SidePanel {

    private StatefulValue<Boolean> expanded = new StatefulValue<>(false);
    private VTree title = new VText("");
    private VTree content = new VNode(HtmlTag.DIV);
    private VTree header = new VText("");

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

    /**
     * The label of the expand button visible when the panel is collapsed.
     */
    public SidePanel expandButtonLabel(String text) {
        return title(new VText(text));
    }

    public VTree build() {
        return new ReactiveComponent("sidepanel", expanded.transform(e -> {
            if(e) {
                return new VNode(HtmlTag.DIV, withClasses("sidepanel"),
                        header(),
                        content());
            } else {
                return new VNode(HtmlTag.DIV, withClasses("sidepanel sidepanel--collapsed"), expandButton());
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
