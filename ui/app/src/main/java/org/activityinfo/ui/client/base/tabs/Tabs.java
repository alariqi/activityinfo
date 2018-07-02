package org.activityinfo.ui.client.base.tabs;

import com.google.common.annotations.VisibleForTesting;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Tabs {

    private static final Logger LOGGER = Logger.getLogger(Tabs.class.getName());

    public static VNode tabPanel(TabItem... tabs) {
        return tabPanel(new StatefulValue<>(0), tabs);
    }

    @VisibleForTesting
    static VNode tabPanel(StatefulValue<Integer> active, TabItem... tabs) {
        return new VNode(HtmlTag.DIV, Props.withClass("tabpanel"),
                selectorButtons(active, tabs),
                body(active, tabs));
    }

    private static VTree selectorButtons(StatefulValue<Integer> active, TabItem[] tabs) {
        return new ReactiveComponent("tabs.buttons", active.transform(activeIndex -> {

            List<VTree> buttons = new ArrayList<>();
            for (int tabIndex = 0; tabIndex < tabs.length; tabIndex++) {

                int finalTabIndex = tabIndex;

                buttons.add(selectorButton(tabs[tabIndex].getLabel(), activeIndex == tabIndex,
                        event -> active.updateIfNotEqual(finalTabIndex)));
            }

            return new VNode(HtmlTag.DIV, Props.withClass("tabpanel__strip"), buttons);
        }));
    }

    private static VTree selectorButton(String label, boolean active, EventHandler clickHandler) {
        PropMap propMap = Props.create();
        propMap.setClass("active", active);
        propMap.onclick(clickHandler);

        return new VNode(HtmlTag.BUTTON, propMap, new VText(label));
    }


    private static VTree body(Observable<Integer> active, TabItem[] tabs) {
        return new ReactiveComponent("tabs.body", active.transform(activeIndex -> {
            return new VNode(HtmlTag.DIV, Props.withClass("tabpanel__body"), tabs[activeIndex].getContent());
        }));
    }
}
