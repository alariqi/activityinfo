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
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.tree.VWidget;

public class FormOverlay extends VWidget {

    private final FormStore formStore;
    private final InputHandler inputHandler;
    private final Observable<FormInputModel> inputModel;

    public FormOverlay(FormStore formStore, Observable<FormInputModel> inputModel, InputHandler inputHandler) {
        this.formStore = formStore;
        this.inputModel = inputModel;
        this.inputHandler = inputHandler;
    }

    @Override
    public IsWidget createWidget() {
        FormInputView view = new FormInputView(formStore, inputModel, inputHandler);
        CssLayoutContainer container = new CssLayoutContainer();
        container.addStyleName("forminput");
        container.addStyleName("forminput--visible");
        container.add(view);
        return container;
    }
}
