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
package org.activityinfo.ui.client.input.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.store.FormStore;

public class FormOverlay implements IsWidget {

    private final FormStore formStore;
    private ResourceId formId;
    private FormInputView formInputView;
    private CssLayoutContainer container;

    public FormOverlay(FormStore formStore) {
        this.formStore = formStore;
        this.container = new CssLayoutContainer();
        this.container.addStyleName("forminput");
        this.container.setVisible(false);
    }

    public void show(RecordRef recordRef) {
        container.clear();
        container.setVisible(true);

        FormInputView view = new FormInputView(formStore, recordRef, new CloseEvent.CloseHandler() {
            @Override
            public void onClose(CloseEvent event) {
                container.setVisible(false);
                container.clear();
            }
        });
        container.add(view);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
