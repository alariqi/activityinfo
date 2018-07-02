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
        return new ReactiveComponent("listtable", listItems.transform(items ->
                div("listtable",
                        items.stream().map(this::renderItem))));
    }

    private VTree renderItem(ListItem item) {
/*
<div class="listtable__item {offlineClass}">
  <a href="{href}" class="listtable__item__label">
    <div class="listtable__item__avatar">
      <svg xmlns="http://www.w3.org/2000/svg" width="18px" height="27px" viewBox="0 0 18 27" class="avatar" preserveAspectRatio="xMinYMin meet">
        <use xlink:href="{avatarHref}"></use>
      </svg>
    </div>
    <span class="listtable__item__title">
      {label}
    </span>
    <span class="listtable__item__offlineindicator">
      available offline
    </span>
  </a>

  <button class="listtable__item__options">
    <svg xmlns="http://www.w3.org/2000/svg" width="21" height="17" viewBox="0 0 21 17" class="icon" preserveAspectRatio="xMinYMin meet">
      <use xlink:href="#options"></use>
    </svg>
    Options
  </button>
</div>

 */
        PropMap labelProps = Props.withClass("listtable__item__label").href(item.getHref());

        return div("listtable__item",
                new VNode(HtmlTag.A, labelProps,
                    div("listtable__item__avatar",
                        item.getAvatar().renderTree()),
                    span("listtable__item__title", item.getLabel())));
    }
}
