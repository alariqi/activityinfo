package org.activityinfo.ui.vdom.shared.html;

import org.activityinfo.ui.vdom.shared.tree.Tag;

public enum SvgTag implements Tag {

    SVG,
    USE;

    @Override
    public boolean isSingleton() {
        return false;
    }
}
