package org.activityinfo.ui.client.base.avatar;

import com.google.gwt.safehtml.shared.SafeHtml;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class Gravatar implements Avatar {
    @Override
    public SafeHtml render() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public VTree renderTree() {

        PropMap imageProps = Props.create();
        imageProps.setClass("avatar");
        imageProps.set("src", "https://www.gravatar.com/avatar/989835256acefc4a5f286184c0e1337b.png");

        return new VNode(HtmlTag.IMG, imageProps);
    }
}
