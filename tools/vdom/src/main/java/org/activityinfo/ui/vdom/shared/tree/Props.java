package org.activityinfo.ui.vdom.shared.tree;

import com.google.gwt.core.client.GWT;
import org.activityinfo.ui.vdom.client.tree.PropMapJs;

public final class Props {

    private Props() {}

    public static PropMap create() {
        if(GWT.isScript()) {
            return PropMapJs.create();
        } else {
            return new PropMapJre();
        }
    }

    /**
     * Creates a new {@code PropMap} with the given style object.
     */
    public static PropMap withStyle(Style style) {
        PropMap propMap = create();
        propMap.setStyle(style);
        return propMap;
    }

    /**
     * Creates a new {@code PropMap} with the given value for the {@code className} property
     */
    public static PropMap withClass(String classes) {
        return create().setClass(classes);
    }
}
