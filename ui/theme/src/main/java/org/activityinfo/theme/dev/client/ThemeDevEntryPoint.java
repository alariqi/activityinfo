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
package org.activityinfo.theme.dev.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class ThemeDevEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {

        DevBundle.RESOURCES.style().ensureInjected();

        TabPanel tabPanel = new TabPanel();
        tabPanel.add(new ButtonsPanel(), "Buttons");
        tabPanel.add(new GridPanel(), "Grid");

        Viewport viewport = new Viewport();
        viewport.add(tabPanel);

        RootPanel.get().add(viewport);
    }
}
