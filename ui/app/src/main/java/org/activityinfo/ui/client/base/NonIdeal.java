package org.activityinfo.ui.client.base;

import com.google.common.base.Strings;
import com.google.gwt.safehtml.shared.UriUtils;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.vdom.shared.html.SvgTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import static org.activityinfo.ui.client.base.button.Buttons.button;
import static org.activityinfo.ui.vdom.shared.html.H.*;

public class NonIdeal {

    public static VTree empty() {
        return svg("","#nis_emptystate");
    }

    public static VNode svg(String href) {
        return svg(null, href);
    }

    public static VNode svg(String classes, String href) {
        return svg(classes, href, "0 0 21 17");
    }

    public static VNode svg(String classes, String href, String viewBox) {
        PropMap svgAttributes = new PropMap()
                .set("viewBox", viewBox)
                .set("preserveAspectRatio", "xMinYMin meet");


        if(!Strings.isNullOrEmpty(classes)){
            svgAttributes.set("class", classes);
        }

        PropMap svgProps = new PropMap()
                .trustedSet("attributes", svgAttributes);


        PropMap useProps = new PropMap()
                .trustedSet("attributes", new PropMap().set("xlink:href", href));

        VNode useElement = new VNode(SvgTag.USE, useProps, VNode.NO_CHILDREN, "", Icon.SVG_NS);

        return new VNode(SvgTag.SVG, svgProps, new VNode[] { useElement }, "", Icon.SVG_NS);
    }

    public static VTree forbidden() {
        /*
        <div class="nonideal nonideal--forbidden">

    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 12 12" class="nonideal__illustration">
        <use xlink:href="#nis_forbidden"></use>
    </svg>

    <h1>{i18n.permissionDeniedHeading}</h1>
    <p>{i18n.permissionDenied}</p>

    <a href="/logout" class="button button--primary">
        <svg xmlns="http://www.w3.org/2000/svg" width="12px" height="12px" viewBox="0 0 12 12" class="icon">
            <use xlink:href="#bubble_user"></use>
        </svg>
        <span class="button__label">{i18n.switchAccounts}</span>
    </a>

    <div class="nonideal__links">
        <a href="#">{i18n.goBackToDatabaseOverview}</a>
    </div>
</div>
         */

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
        /*
        <div class="nonideal nonideal--notfound">

    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 12 12" class="nonideal__illustration">
        <use xlink:href="#nis_notfound"></use>
    </svg>

    <h1>{i18n.dataNotFound}</h1>
    <p>This could be because:</p>
    <ul>
        <li>It has been deleted</li>
        <li>There is a typo in the address</li>
    </ul>

    <div class="nonideal__links">
        <a href="#">{i18n.goBackToDatabaseOverview}</a>
    </div>
</div>
         */

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

    private static VNode illustration(String href) {
        return svg("nonideal__illustration", href, "0 0 12 12");
    }

    private static VNode backToDatabaseOverviewLink() {
        return link(DatabaseListPlace.INSTANCE.toUri(), t(I18N.CONSTANTS.goBackToDatabaseOverview()));
    }

}
