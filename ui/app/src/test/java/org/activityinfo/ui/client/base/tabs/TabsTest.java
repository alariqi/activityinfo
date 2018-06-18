package org.activityinfo.ui.client.base.tabs;

import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.vdom.shared.diff.Diff;
import org.activityinfo.ui.vdom.shared.diff.VPatchSet;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlRenderer;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;
import org.junit.Test;

public class TabsTest {

    @Test
    public void test() {

        StatefulValue<Integer> tabIndex = new StatefulValue<>(0);
        StatefulValue<VTree> a = new StatefulValue<>(H.div("Body A"));
        StatefulValue<VTree> b = new StatefulValue<>(H.div("Body B"));

        VNode tabPanel = Tabs.tabPanel(tabIndex,
                new TabItem("A", new ReactiveComponent(a)),
                new TabItem("B", new ReactiveComponent(b)));

        System.out.println("BEFORE");
        System.out.println(HtmlRenderer.render(tabPanel));

        VPatchSet initial = Diff.diff(new VNode(HtmlTag.DIV), tabPanel);

        tabIndex.updateValue(1);

        VPatchSet patchSet = Diff.diff(tabPanel, tabPanel);

        System.out.println(patchSet);


        System.out.println("AFTER");
        System.out.println(HtmlRenderer.render(tabPanel));

        tabIndex.updateValue(0);


        System.out.println("AFTER2");
        System.out.println(HtmlRenderer.render(tabPanel));

    }

}