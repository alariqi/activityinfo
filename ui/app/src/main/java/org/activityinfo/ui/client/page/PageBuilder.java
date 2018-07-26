package org.activityinfo.ui.client.page;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.Place;
import org.activityinfo.ui.client.base.avatar.Avatar;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.activityinfo.ui.vdom.shared.html.H.*;

public class PageBuilder {

    private Observable<List<Breadcrumb>> breadcrumbs;
    private Avatar avatar;
    private VTree heading;
    private VTree body;
    private Place parentPlace;
    private List<VTree> actions = new ArrayList<>();

    private boolean padded = false;

    public PageBuilder heading(String text) {
        this.heading = new VText(text);
        return this;
    }

    public PageBuilder heading(Observable<String> heading) {
        this.heading = new ReactiveComponent("page.heading", heading.transform(VText::new));
        return this;
    }

    public PageBuilder breadcrumbs(Breadcrumb... breadcrumbs) {
        return breadcrumbs(Arrays.asList(breadcrumbs));
    }

    public PageBuilder breadcrumbs(List<Breadcrumb> breadcrumbs) {
        this.breadcrumbs = Observable.just(breadcrumbs);
        return this;
    }

    public PageBuilder breadcrumbs(Observable<List<Breadcrumb>> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
        return this;
    }

    public PageBuilder headerAction(VTree tree) {
        this.actions.add(tree);
        return this;
    }

    public PageBuilder padded() {
        this.padded = true;
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

        String classes = "page";
        if (padded) {
            classes += " page--padded";
        } else {
            classes += " page--fullwidth";
        }

        return div(classes,
                header(),
                body());
    }


    private VTree header() {
        return div("page__header",
                div("page__header__inner", nullableList(
                    maybeParentLink(),
                    maybeAvatar(),
                    div("page__header__content",
                        div("page__header__titles",
                          nullableList(
                            breadcrumbs(),
                            h1(heading)))
                    ),
                    maybeActions()
                )));
    }


    private VNode body() {
        if(padded) {
            return div("page__body",
                    div("page__body__inner", body));
        } else {
            return div("page__body", body);
        }
    }


    private VTree maybeParentLink() {
        if(parentPlace != null) {
            return Buttons.button(I18N.CONSTANTS.backButton())
                    .icon(Icon.BUBBLE_ARROWLEFT)
                    .link(parentPlace.toUri())
                    .build();
        } else {
            return null;
        }
    }


    private VTree maybeAvatar() {
        if(avatar != null) {
            return div("page__header__avatar", avatar.render());
        } else {
            return null;
        }
    }

    private VTree breadcrumbs() {
        if(breadcrumbs == null) {
            return null;
        }
        return new ReactiveComponent("breadcrumbs",
                breadcrumbs.transform(list ->
                    ul("breadcrumbs", list.stream().map(Breadcrumb::render))));
    }

    private VTree maybeActions() {
        if(actions.isEmpty()) {
            return null;
        }
        return H.div("page__header__actions", actions.stream());
    }
}
