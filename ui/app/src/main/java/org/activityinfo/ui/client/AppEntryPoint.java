/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import org.activityinfo.api.client.ActivityInfoClientAsync;
import org.activityinfo.api.client.ActivityInfoClientAsyncImpl;
import org.activityinfo.indexedb.IDBFactoryImpl;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.storage.LocalStorage;
import org.activityinfo.ui.client.database.DatabaseListPage;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.client.database.DatabasePage;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header2;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.store.FormStoreImpl;
import org.activityinfo.ui.client.store.http.ConnectionListener;
import org.activityinfo.ui.client.store.http.HttpStore;
import org.activityinfo.ui.client.store.offline.OfflineStore;
import org.activityinfo.ui.client.store.offline.RecordSynchronizer;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.view.TableView;
import org.activityinfo.ui.vdom.client.VDomWidget;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.logging.Logger;

/**
 * GWT EntryPoint that starts the application.
 */
public class AppEntryPoint implements EntryPoint {

    private static final Logger LOGGER = Logger.getLogger(AppEntryPoint.class.getName());


    @Override
    public void onModuleLoad() {

        LOGGER.info("user.agent = " + System.getProperty("user.agent"));
        LOGGER.info("gxt.user.agent = " + System.getProperty("gxt.user.agent"));
        LOGGER.info("gxt.device = " + System.getProperty("gxt.device"));

        injectStyle();
        injectIcons();

        AppCache appCache = new AppCache();
        AppCacheMonitor3 monitor = new AppCacheMonitor3(appCache);
        monitor.start();

        ConnectionListener connectionListener = new ConnectionListener();
        connectionListener.start();

        ActivityInfoClientAsync client = new ActivityInfoClientAsyncImpl(findServerUrl());
        HttpStore httpStore = new HttpStore(connectionListener.getOnline(), client, Scheduler.get());

        OfflineStore offlineStore = new OfflineStore(httpStore, IDBFactoryImpl.create());

        FormStore formStore = new FormStoreImpl(httpStore, offlineStore, Scheduler.get());
        LocalStorage storage = LocalStorage.create();

        // Start synchronizer...
        RecordSynchronizer synchronizer = new RecordSynchronizer(httpStore, offlineStore);


        StatefulValue<Place2> place = new StatefulValue<>(DatabaseListPlace.INSTANCE);
        AppPlaceHistoryMapper historyMapper = new AppPlaceHistoryMapper();
        History.addValueChangeHandler(event ->
                place.updateIfNotSame(historyMapper.apply(event.getValue())));

        History.fireCurrentHistoryState();



        VDomWidget appFrame = new VDomWidget();
        appFrame.update(renderApp(formStore, place));

        hideLoader();

        RootPanel.get().add(appFrame);

    }

    public static void injectStyle() {
        StyleInjector.inject(getStylesheet().getText(), true);
    }

    private static TextResource getStylesheet() {
        if(LocaleInfo.getCurrentLocale().isRTL()) {
            return ThemeBundle.INSTANCE.stylesheetRtl();
        } else {
            return ThemeBundle.INSTANCE.stylesheet();
        }
    }

    public static void injectIcons() {
        DivElement divElement = Document.get().createDivElement();
        divElement.setInnerHTML(ThemeBundle.INSTANCE.icons().getText());
        Element svgElement = divElement.getFirstChildElement();
        Document.get().getBody().insertFirst(svgElement);
    }

    /**
     * Hides the loading indicator that is displayed while the application is loading. The html/css for the
     * loading indicator can be found in {@code Host4.ftl}
     */
    private void hideLoader() {
        Document.get().getElementById("initial-loader").removeFromParent();
    }

    private String findServerUrl() {
        if (Window.Location.getHostName().equals("localhost")) {
            return "http://localhost:8080/resources";
        } else {
            return "/resources";
        }
    }

    private VTree renderApp(FormStore formStore, StatefulValue<Place2> place) {
        VTree header = Header2.render(formStore);
        VTree connectionStatus = ConnectionStatus.render();
        VTree page = new ReactiveComponent(place.cache().transform(p -> {

            LOGGER.info("Place changed:"  + p);

            if(p instanceof DatabaseListPlace) {
                return DatabaseListPage.render(formStore);
            } else if(p instanceof DatabasePlace) {
                return DatabasePage.render(formStore, ((DatabasePlace) p));
            } else if(p instanceof TablePlace) {
                return TableView.render(formStore, (TablePlace) p);
            } else {
                return H.div("Nope.");
            }
        }));


        return H.div("appframe appframe--fixed",
                header,
                connectionStatus,
                page);
    }

}
