package org.activityinfo.ui.client.base;

import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class Loader {

    private String classes = "loader";

    public Loader small() {
        classes += " loader--small";
        return this;
    }

    public Loader dark() {
        classes += " loader--dark";
        return this;
    }

    public VTree build() {
        return H.div(classes,
                H.div("loader__blob"),
                H.div("loader__blob"),
                H.div("loader__blob"));
    }
}
