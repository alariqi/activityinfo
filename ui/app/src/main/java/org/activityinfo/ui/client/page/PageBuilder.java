package org.activityinfo.ui.client.page;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class PageBuilder {

    private Observable<List<Breadcrumb>> breadcrumbs;
    private Avatar avatar;
    private VTree heading;
    private VTree body;

    public PageBuilder heading(String text) {
        this.heading = new VText(text);
        return this;
    }

    public PageBuilder heading(Observable<String> heading) {
        this.heading = new ReactiveComponent("page.heading", heading.transform(VText::new));
        return this;
    }

    public PageBuilder breadcrumbs(List<Breadcrumb> breadcrumbs) {
        this.breadcrumbs = Observable.just(breadcrumbs);
        return this;
    }

    public PageBuilder breadcrumbs(Observable<List<Breadcrumb>> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
        return this;
    }

    public PageBuilder avatar(Avatar avatar) {
        this.avatar = avatar;
        return this;
    }

    public PageBuilder body(VTree vTree) {
        this.body = vTree;
        return this;
    }

    public VTree build() {
        return div("page page--fullwidth",
                header(),
                div("page__body", body));
    }

    public VTree header() {
        return div("page__header",
                div("page__header__inner", nullableList(
                    maybeAvatar(),
                    div("page__header__content",
                        div("page__header__titles",
                            breadcrumbs(),
                            h1(heading))
                    ),
                    maybeActions()
                )));
    }

    private VTree maybeAvatar() {
        if(avatar != null) {
            return div("page__header__avatar", avatar.renderTree());
        } else {
            return null;
        }
    }

    private VTree breadcrumbs() {
        return new ReactiveComponent("breadcrumbs",
                breadcrumbs.transform(list ->
                    ul("breadcrumbs", list.stream().map(Breadcrumb::render))));
    }

    private VTree maybeActions() {
        return null;
    }
}
