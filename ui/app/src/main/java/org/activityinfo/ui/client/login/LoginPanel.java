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
package org.activityinfo.ui.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;


public class LoginPanel implements IsWidget {

    interface MyUiBinder extends UiBinder<Widget, LoginPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private final Widget panel;

    @UiField
    TextButton loginButton;
    @UiField
    FramedPanel frame;
    @UiField
    TextField email;
    @UiField
    PasswordField password;

    public LoginPanel() {
        panel = uiBinder.createAndBindUi(this);

    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @UiHandler("loginButton")
    public void onLogin(final SelectEvent e) {

        enable(false);

        RequestBuilder request = new RequestBuilder(RequestBuilder.POST, "https://www.activityinfo.org/login/ajax");
        request.setHeader("Content-type", "application/x-www-form-urlencoded");
        request.setRequestData("email=" + email.getValue() + "&password=" + password.getCurrentValue());
        request.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                enable(true);
                if(response.getStatusCode() == 200) {
                    Window.alert("Login success!\n" + response.getText());
                } else {
                    Window.alert(response.getText());
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                enable(false);
                Window.alert("Login failed");
            }
        });
        try {
            request.send();
        } catch (RequestException e1) {
            Window.alert("Exception sending request");
        }
    }

    private void enable(boolean enabled) {
        email.setEnabled(enabled);
        password.setEnabled(enabled);
        loginButton.setEnabled(enabled);
    }
}
