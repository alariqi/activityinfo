package org.activityinfo.ui.client.base.avatar;

import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class Gravatar implements Avatar {

    @Override
    public VTree render() {

        PropMap imageProps = Props.create();
        imageProps.setClass("avatar");
        imageProps.set("src", avatarUrl());

        return new VNode(HtmlTag.IMG, imageProps);
    }

    private String avatarUrl() {
        return "/resources/profile/avatar";
    }
}
    