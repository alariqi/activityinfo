package org.activityinfo.ui.client.base.cardlist;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;
import java.util.function.Function;

public class CardList<T> {


    private final Observable<List<T>> items;
    private Function<T, VTree> renderer;
    private String emptyText;

    public CardList<T> builder(Observable<List<T>> items) {
        return new CardList<>(items);
    }

    public CardList(Observable<List<T>> items) {
        this.items = items;
    }

    public CardList<T> setRenderer(Function<T, VTree> renderer) {
        this.renderer = renderer;
        return this;
    }

    public CardList<T> setEmptyText(String emptyText) {
        this.emptyText = emptyText;
        return this;
    }

    public VTree build() {
        return new ReactiveComponent(items.transform(list -> {
            if(list.isEmpty()) {
                return renderEmpty();
            } else {
                return renderList(list);
            }
        }));
    }

    private VTree renderEmpty() {
        return H.div("cardlist cardlist--empty", new VText(emptyText));
    }

    private VTree renderList(List<T> list) {
        return H.div("cardlist cardlist--empty",
                list.stream().map(this::renderItem));
    }

    private VTree renderItem(T t) {
        return null;
    }

}
