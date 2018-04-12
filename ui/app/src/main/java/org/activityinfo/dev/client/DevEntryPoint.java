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
package org.activityinfo.dev.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class DevEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {

        DevBundle.RESOURCES.style().ensureInjected();

        EventBus eventBus = new SimpleEventBus();
        PlaceController placeController = new PlaceController(eventBus);

        Viewport viewport = new Viewport();

        ActivityMapper activityMapper = place -> new DevActivity((DevPlace) place);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(child -> {
            viewport.setWidget(child);
            Scheduler.get().scheduleFinally(() -> viewport.forceLayout());
        });

        PlaceHistoryMapper historyMapper = new PlaceHistoryMapper() {
            @Override
            public Place getPlace(String token) {
                return new DevPlace(DevPage.valueOf(token));
            }

            @Override
            public String getToken(Place place) {
                return ((DevPlace) place).getPage().name();
            }
        };
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, new DevPlace(DevPage.INDEX));

        RootPanel.get().add(viewport);

        historyHandler.handleCurrentHistory();
    }
}
