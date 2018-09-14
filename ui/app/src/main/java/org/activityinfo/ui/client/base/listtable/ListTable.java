package org.activityinfo.ui.client.base.listtable;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.List;

import static org.activityinfo.ui.vdom.shared.html.H.div;
import static org.activityinfo.ui.vdom.shared.html.H.span;

public class ListTable {

    private VTree emptyState;
    private Observable<List<ListItem>> listItems;

    public ListTable(Observable<List<ListItem>> listItems) {
        this.listItems = listItems;
    }

    public ListTable(List<ListItem> items) {
        this.listItems = Observable.just(items);
    }

    public ListTable emptyState(VTree vTree) {
        this.emptyState = vTree;
        return this;
    }

    public VTree render() {
        return new ReactiveComponent("listtable", listItems.transform(items -> {
            if(items.isEmpty()) {
                return emptyState;
            } else {
                return div("listtable",
                        items.stream().map(this::renderItem));
            }
        }));
    }

    private VTree renderItem(ListItem item) {

        PropMap labelProps = Props.withClass("listtable__item__label").href(item.getHref());

        return div("listtable__item",
                new VNode(HtmlTag.A, labelProps,
                    div("listtable__item__avatar",
                        item.getAvatar().render()),
                    span("listtable__item__title", item.getLabel())));
    }
}
