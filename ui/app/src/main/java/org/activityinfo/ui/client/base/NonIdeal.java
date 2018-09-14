package org.activityinfo.ui.client.base;

import com.google.gwt.safehtml.shared.UriUtils;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import static org.activityinfo.ui.client.base.button.Buttons.button;
import static org.activityinfo.ui.vdom.shared.html.H.*;

public class NonIdeal {

    public static VTree empty() {
        return Svg.svg("","#nis_emptystate");
    }

    public static VTree forbidden() {
        return div("nonideal nonideal--forbidden",
                illustration("#nis_forbidden"),
                h1(I18N.CONSTANTS.permissionDeniedHeading()),
                p(I18N.CONSTANTS.permissionDenied()),
                switchAccountsButton(),
                div("nonideal__links",
                        backToDatabaseOverviewLink()));
    }

    private static VTree switchAccountsButton() {
        return button(I18N.CONSTANTS.switchAccounts())
                .link(UriUtils.fromTrustedString("/logout"))
                .icon(Icon.BUBBLE_USER)
                .primary()
                .build();
    }


    public static VTree notFound() {
        return div("nonideal nonideal--notfound",
                illustration("#nis_notfound"),
                h1(I18N.CONSTANTS.dataNotFound()),
                p(I18N.CONSTANTS.dataNotFoundReason()),
                ul(
                    li(I18N.CONSTANTS.dataNotFoundReasonDeleted()),
                    li(I18N.CONSTANTS.dataNotFoundReasonTypo())
                ),
                div("nonideal__links",
                        backToDatabaseOverviewLink()));
    }


    public static VNode illustration(String href) {
        return Svg.svg("nonideal__illustration", href, "0 0 12 12");
    }

    private static VNode backToDatabaseOverviewLink() {
        return link(DatabaseListPlace.INSTANCE.toUri(), t(I18N.CONSTANTS.goBackToDatabaseOverview()));
    }

}
