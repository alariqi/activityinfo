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

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.activityinfo.api.client.ActivityInfoClientAsync;
import org.activityinfo.api.client.ActivityInfoClientAsyncImpl;
import org.activityinfo.indexedb.IDBFactoryImpl;
import org.activityinfo.storage.LocalStorage;
import org.activityinfo.theme.client.ThemeBundle;
import org.activityinfo.ui.client.databases.DatabaseListPlace;
import org.activityinfo.ui.client.header.Frame;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.store.FormStoreImpl;
import org.activityinfo.ui.client.store.http.ConnectionListener;
import org.activityinfo.ui.client.store.http.HttpStore;
import org.activityinfo.ui.client.store.offline.OfflineStore;
import org.activityinfo.ui.client.store.offline.RecordSynchronizer;

import java.util.logging.Logger;

/**
 * GWT EntryPoint that starts the application.
 */
public class AppEntryPoint implements EntryPoint {

    public static final Place DEFAULT_PLACE = new DatabaseListPlace();

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

        EventBus eventBus = new SimpleEventBus();
        PlaceController placeController = new PlaceController(eventBus);

        ConnectionListener connectionListener = new ConnectionListener();
        connectionListener.start();

        ActivityInfoClientAsync client = new ActivityInfoClientAsyncImpl(findServerUrl());
        HttpStore httpStore = new HttpStore(connectionListener.getOnline(), client, Scheduler.get());

        OfflineStore offlineStore = new OfflineStore(httpStore, IDBFactoryImpl.create());

        FormStore formStore = new FormStoreImpl(httpStore, offlineStore, Scheduler.get());
        LocalStorage storage = LocalStorage.create();

        Frame frame = new Frame(formStore);

        ActivityMapper activityMapper = new AppActivityMapper(formStore, storage);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(frame);

        AppPlaceHistoryMapper historyMapper = new AppPlaceHistoryMapper();
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, DEFAULT_PLACE);

        // Start synchronizer...
        RecordSynchronizer synchronizer = new RecordSynchronizer(httpStore, offlineStore);


        RootPanel.get().add(frame);

        historyHandler.handleCurrentHistory();

    }

    public static void injectStyle() {
        //     <link rel="stylesheet" type="text/css" href="App/app.css">
        LinkElement link = Document.get().createLinkElement();
        link.setRel("stylesheet");
        link.setType("text/css");
        link.setHref(GWT.getModuleBaseForStaticFiles() + "app.css");

        Document.get().getHead().appendChild(link);
    }

    public static void injectIcons() {
        DivElement divElement = Document.get().createDivElement();
        divElement.setInnerHTML(ThemeBundle.INSTANCE.icons().getText());
        Element svgElement = divElement.getFirstChildElement();
        Document.get().getBody().insertFirst(svgElement);
    }

    private String findServerUrl() {
        if (Window.Location.getHostName().equals("localhost")) {
            return "http://localhost:8080/resources";
        } else {
            return "/resources";
        }
    }
}
