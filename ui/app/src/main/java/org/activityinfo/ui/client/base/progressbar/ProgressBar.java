package org.activityinfo.ui.client.base.progressbar;

import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class ProgressBar {

    public static VTree bar(int numerator, int denominator, String message) {
        double percentage = ((double)numerator)/((double)denominator) *100d;
        return bar((int)Math.round(percentage), message);
    }

    private static VTree bar(int percentage, String message) {
        return H.div("progress",
                H.div("progress__bar",
                        progressCompleteBar(percentage)),
                H.div("progress__message",
                        new VText(message),
                        percentage(percentage)));

    }

    private static VTree progressCompleteBar(int percentage) {

        PropMap style = Props.create();
        style.set("width", percentage + "%");

        PropMap props = Props.create();
        props.setClass("progress__complete");
        props.setStyle(style);

        return H.div(props);
    }

    private static VTree percentage(int percentage) {
        return H.div("progress__percent", new VText(percentage + "%"));
    }
}
