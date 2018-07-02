package org.activityinfo.ui.client.header;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.Place2;
import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.client.base.avatar.Avatar;
import org.activityinfo.ui.client.base.avatar.Gravatar;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.client.reports.ReportListPlace;
import org.activityinfo.ui.client.search.SearchWidget;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import static org.activityinfo.ui.vdom.shared.html.H.div;
import static org.activityinfo.ui.vdom.shared.html.H.span;

public class Header2 {

    public static VTree render(FormStore formStore) {

        return new VNode(HtmlTag.HEADER,
                logo(),
                new SearchWidget(formStore),
                new VNode(HtmlTag.NAV,
                        databases(),
                        reports(),
                        settings()));
    }



    private static VTree logo() {
        return div("logo",
                Svg.svg("#logo"),
                span("logo__name", "ActivityInfo"));
    }


    private static VTree databases() {
        return navButton(Icon.HEADER_DATABASES, I18N.CONSTANTS.databases(), DatabaseListPlace.INSTANCE);
    }

    private static VTree reports() {
        return navButton(Icon.HEADER_REPORTS, I18N.CONSTANTS.reports(),
                new ReportListPlace());
    }

    private static VTree navButton(Icon icon, String label, Place2 place) {
        PropMap buttonProps = Props
                .withClass("nav__button")
                .href(place.toUri());

        return new VNode(HtmlTag.A, buttonProps,
                icon.tree(), H.span("button__label", label));

    }


    private static VTree settings() {
        Avatar avatar = new Gravatar();

        return new VNode(HtmlTag.BUTTON,
                Props.withClass("profile"),
                avatar.renderTree(),
                Icon.EXPAND_DOWN.tree());

    }
}
