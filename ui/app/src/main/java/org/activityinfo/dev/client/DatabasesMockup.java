package org.activityinfo.dev.client;

import com.google.common.collect.Lists;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.observable.Observable;
import org.activityinfo.theme.client.CssLayoutContainer;
import org.activityinfo.ui.client.databases.DatabaseListPage;
import org.activityinfo.ui.client.databases.ListItem;
import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header;

import java.util.List;

public class DatabasesMockup implements IsWidget {

    private CssLayoutContainer container;

    public DatabasesMockup() {

        DatabaseListPage page = new DatabaseListPage();
        page.updateView(Observable.just(databaseList()));

        this.container = new CssLayoutContainer();
        this.container.add(new Header(SearchResults.getResourceList()));
        this.container.add(new ConnectionStatus());
        this.container.add(page);
    }

    private List<ListItem> databaseList() {
        return Lists.newArrayList(
                result("7003010d", ".Development Database - Iraq"),
                result("2355010d", "2015: IRQ Internally Displaced Persons (IDPs)"),
                result("5131010d", "2016: IRQ Internally Displaced Persons (IDPs)"),
                result("6618010d", "2017 : IRAQ IDP"),
                result("8704010d", "2018 Test"),
                result("8454010d", "2018: IRAQ IDP"),
                result("8810010d", "2018: WASH Training"),
                result("9064010d", "2018: WHO Medical Technology"),
                result("8089010d", "IRAQ-2018-Reference"));
    }

    private ListItem result(String id, String name) {
        return new ListItem(id, name, UriUtils.fromSafeConstant("#" + DevPage.DATABASE_PAGE.name()), "#database");
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
