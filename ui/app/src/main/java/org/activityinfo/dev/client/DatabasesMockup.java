package org.activityinfo.dev.client;

import com.google.common.collect.Lists;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.listtable.ListItem;
import org.activityinfo.ui.client.database.DatabaseListPage;
import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabasesMockup implements IsWidget {

    private CssLayoutContainer container;

    private DatabasesMockup(List<ListItem> items) {

        DatabaseListPage page = new DatabaseListPage();
        page.updateView(Observable.just(items));

        this.container = new CssLayoutContainer();
        this.container.add(new Header(SearchResults.getResourceList()));
        this.container.add(new ConnectionStatus());
        this.container.add(page);
    }

    public static DatabasesMockup smallList() {
        ArrayList<ListItem> dbs = Lists.newArrayList(
                result("7003010d", ".Development Database - Iraq"),
                result("2355010d", "2015: IRQ Internally Displaced Persons (IDPs)"),
                result("5131010d", "2016: IRQ Internally Displaced Persons (IDPs)"),
                result("6618010d", "2017 : IRAQ IDP"),
                result("8704010d", "2018 Test"),
                result("8454010d", "2018: IRAQ IDP"),
                result("8810010d", "2018: WASH Training"),
                result("9064010d", "2018: WHO Medical Technology"),
                result("8089010d", "IRAQ-2018-Reference"));

        return new DatabasesMockup(dbs);
    }

    public static DatabasesMockup emptyList() {
        return new DatabasesMockup(Collections.emptyList());
    }

    private static ListItem result(String id, String name) {
        return new ListItem(id, name, UriUtils.fromSafeConstant("#" + DevPage.DATABASE_PAGE.name()),
                "#type_database", name.contains("2018"));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
