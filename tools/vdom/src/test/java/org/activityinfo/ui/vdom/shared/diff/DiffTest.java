package org.activityinfo.ui.vdom.shared.diff;

import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.vdom.shared.html.HtmlRenderer;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.activityinfo.ui.vdom.shared.html.H.*;
import static org.activityinfo.ui.vdom.shared.html.HtmlTag.UL;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DiffTest {

    @Test
    public void inserts() {

        VTree a, b;

        a = div(className("red"),         // 0
                p(                        // 1
                    "Hello world."),      // 2
                p(                        // 3
                    "I have a list"),     // 4
                ul(),                     // 5
                p(                        // 6
                    "That was my list")); // 7


        b = div(className("red"),
                p("Hello world."),
                p("I have a list"),
                ul(
                    li("Item 1"),
                    li("Item 2"),
                    li("Item 3"),
                    li("Item 4")
                ),
                p("That was my list"));


        // Find the differences between the two trees
        VPatchSet diff = Diff.diff(a, b);
        System.out.println(diff);

        // The only node to be patched should be the UL at index #5,
        // the parent where we need to add the new children
        assertThat(diff.getPatchedIndexes(), Matchers.contains(5));

        // Verify that we have three INSERTS
        assertThat(diff.get(5), hasSize(4));

        // With the right nodes...
        VNode updatedList = (VNode) b.childAt(2);
        assertThat(updatedList.tag, is((Tag) UL));
        assertThat(diff.get(5), contains(
                insert(updatedList.childAt(0)),
                insert(updatedList.childAt(1)),
                insert(updatedList.childAt(2)),
                insert(updatedList.childAt(3))));
    }

    private enum Shape { RECT, CIRCLE };

    private static class Model {
        Shape shape;
        String label;

        public Model(Shape shape, String label) {
            this.shape = shape;
            this.label = label;
        }
    }

    @Test
    public void reactiveComponentDiff() {

        StatefulValue<Model> model = new StatefulValue<>(new Model(Shape.RECT, "Hello"));

        VTree tree = new VNode(HtmlTag.DIV,
                new ReactiveComponent(model.transform(m -> {
                    if(m.shape == Shape.RECT) {
                        return new VNode(HtmlTag.DIV, PropMap.withClasses("rect"), new VText(m.label));
                    } else {
                        return new VNode(HtmlTag.DIV, PropMap.withClasses("circle"), new VText(m.label));
                    }
                })));

        ensureTree(tree);
        dumpHtml(tree);

        model.updateValue(new Model(Shape.RECT, "Goodbye"));

        VPatchSet patchSet = Diff.diff(tree, tree);

        System.out.println(patchSet);
    }

    private void ensureTree(VTree tree) {
        if(tree instanceof VComponent) {
            ((VComponent) tree).ensureRendered();
        } else if(tree instanceof VNode) {
            for (VTree child : ((VNode) tree).children) {
                ensureTree(child);
            }
        }
    }

    private void dumpHtml(VTree tree) {
        System.out.println(HtmlRenderer.render(tree));
    }

    private PatchOp insert(VTree node) {
        return new InsertOp(node);
    }
}