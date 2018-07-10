package org.activityinfo.ui.client.base.avatar;

import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public enum GenericAvatar implements Avatar {

    DATABASE,
    FORM,
    FOLDER;

    @Override
    public VTree render() {
        return Svg.svg("avatar", "#type_" + name().toLowerCase(), "0 0 18 27");
    }

    public String href() {
        return "#type_" + name().toLowerCase();
    }
}
