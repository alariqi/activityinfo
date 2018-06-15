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
package org.activityinfo.ui.client.table.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.base.button.CloseButton;
import org.activityinfo.ui.client.base.button.PlainTextButton;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;

/**
 * Sidebar panel containing details, history, etc.
 */
public class SidePanel implements IsWidget {

    private final CssLayoutContainer container;

    private boolean collapsed = true;

    public SidePanel(IsWidget content) {

        PlainTextButton expandButton = new PlainTextButton("Details & History ▲");
        expandButton.addStyleName("sidepanel__expand");
        expandButton.addSelectHandler(e -> expandPanel());

        PlainTextButton scrollButton = new PlainTextButton("Scroll to this record...");
        scrollButton.addStyleName("sidepanel__scrollto");

        CloseButton collapseButton = new CloseButton();
        collapseButton.addStyleName("sidepanel__collapse");
        collapseButton.addSelectHandler(e -> collapsePanel());

        CssLayoutContainer inner = new CssLayoutContainer();
        inner.addStyleName("sidepanel__content");
        inner.add(content);

        container = new CssLayoutContainer("aside");
        container.addStyleName("sidepanel");
        container.addStyleName("sidepanel--collapsed");
        container.add(expandButton);
        container.add(scrollButton);
        container.add(collapseButton);
        container.add(inner);
    }

    public void expandPanel() {
        if(collapsed) {
            container.removeStyleName("sidepanel--collapsed");
            collapsed = false;
        }
    }

    private void collapsePanel() {
        container.addStyleName("sidepanel--collapsed");
        collapsed = true;
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
