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
package org.activityinfo.ui.client.page.common.toolbar;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.ui.Image;
import org.activityinfo.ui.client.style.legacy.icon.IconImageBundle;

public class WarningBar extends LayoutContainer {
    private LabelField labelMessage;
    private Image imageLocked;
    private HorizontalPanel panelMain;

    public WarningBar() {
        super();

        initializeComponent();
    }

    private void initializeComponent() {
        panelMain = new HorizontalPanel();
        add(panelMain);

        imageLocked = IconImageBundle.ICONS.lockedPeriod().createImage();
        panelMain.add(imageLocked);

        labelMessage = new LabelField();
        panelMain.add(labelMessage);

        setStyleAttribute("padding", "0.25em");
        setStyleAttribute("background-color", "#F5A9A9");
    }

    public void setWarning(String message) {
        labelMessage.setText(message);
    }

}
