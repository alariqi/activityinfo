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
package org.activityinfo.ui.client.page.app;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import org.activityinfo.i18n.shared.ApplicationLocale;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.AuthenticatedUser;
import org.activityinfo.legacy.shared.Log;
import org.activityinfo.legacy.shared.command.GetUserProfile;
import org.activityinfo.legacy.shared.command.UpdateUserProfile;
import org.activityinfo.legacy.shared.command.result.VoidResult;
import org.activityinfo.legacy.shared.model.UserProfileDTO;
import org.activityinfo.ui.client.LocaleSwitcher;
import org.activityinfo.ui.client.dispatch.Dispatcher;
import org.activityinfo.ui.client.inject.ClientSideAuthProvider;
import org.activityinfo.ui.client.page.*;
import org.activityinfo.ui.client.widget.legacy.MappingComboBox;
import org.activityinfo.ui.client.widget.legacy.MappingComboBoxBinding;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserProfilePage extends FormPanel implements Page {
    public static final PageId PAGE_ID = new PageId("userprofile");

    private final Dispatcher dispatcher;
    private final Anchor localeLink;
    private FormBinding binding;

    private UserProfileDTO userProfile;
    private final AdapterField localeLinkField;
    private final MappingComboBox<String> localeCombo;

    @Inject
    public UserProfilePage(final Dispatcher dispatcher) {
        super();
        this.dispatcher = dispatcher;

        this.setHeadingText(I18N.CONSTANTS.userProfile());

        this.hide(); // avoid showing an unbound form, will be shown when bound

        binding = new FormBinding(this);

        TextField<String> nameField = new TextField<String>();
        nameField.setAllowBlank(false);
        nameField.setFieldLabel(I18N.CONSTANTS.name());
        nameField.setMaxLength(50);
        nameField.addListener(Events.Change, new ProfileChangeListener("name"));
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
        this.add(nameField);

        TextField<String> organizationField = new TextField<String>();
        organizationField.setFieldLabel(I18N.CONSTANTS.organization());
        organizationField.setMaxLength(100);
        organizationField.addListener(Events.Change, new ProfileChangeListener("organization"));
        binding.addFieldBinding(new FieldBinding(organizationField, "organization"));
        this.add(organizationField);

        TextField<String> jobtitleField = new TextField<String>();
        jobtitleField.setFieldLabel(I18N.CONSTANTS.jobtitle());
        jobtitleField.setMaxLength(100);
        jobtitleField.addListener(Events.Change, new ProfileChangeListener("jobtitle"));
        binding.addFieldBinding(new FieldBinding(jobtitleField, "jobtitle"));
        this.add(jobtitleField);

        CheckBox emailNotification = new CheckBox();
        emailNotification.setFieldLabel(I18N.CONSTANTS.emailNotification());
        emailNotification.addListener(Events.Change, new ProfileChangeListener("emailNotification"));
        binding.addFieldBinding(new FieldBinding(emailNotification, "emailNotification"));
        this.add(emailNotification);

        localeCombo = new MappingComboBox<String>();
        for (ApplicationLocale locale : ApplicationLocale.values()) {
            localeCombo.add(locale.getCode(), locale.getLocalizedName());
        }
        localeCombo.setFieldLabel(I18N.CONSTANTS.language());
        localeCombo.setEditable(false);
        localeCombo.setAllowBlank(false);
        binding.addFieldBinding(new MappingComboBoxBinding(localeCombo, "locale"));
        localeCombo.addListener(Events.Select, new LocaleChangeListener());
        this.add(localeCombo);

        localeLink = new Anchor();
        localeLinkField = new AdapterField(localeLink);
        localeLinkField.setVisible(false);
        localeLinkField.setLabelSeparator("");
        this.add(localeLinkField);

        Label supportMessage = new Label("\n" + I18N.CONSTANTS.requestDeleteChangeEmail());
        supportMessage.setStyleAttribute("font-family", "sans-serif");
        supportMessage.setStyleAttribute("font-size", "small");
        this.add(supportMessage);
        
        bindProfile();
    }

    private void bindProfile() {
        userProfile = new UserProfileDTO();
        AuthenticatedUser user = new ClientSideAuthProvider().get();
        dispatcher.execute(new GetUserProfile(user.getUserId()), new AsyncCallback<UserProfileDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                Log.error("error binding profile", caught);
                MessageBox.alert(I18N.CONSTANTS.serverError(), caught.getMessage(), null);
            }

            @Override
            public void onSuccess(UserProfileDTO userProfileDTO) {
                userProfile = userProfileDTO;
                binding.bind(userProfile);
                UserProfilePage.this.show();
                maybeShowSwitchLocaleLink();
            }
        });
    }

    private void saveProfile() {
        AuthenticatedUser user = new ClientSideAuthProvider().get();
        if (user != null && user.getUserId() == userProfile.getUserId() && isValid()) {
            dispatcher.execute(new UpdateUserProfile(userProfile), new AsyncCallback<VoidResult>() {
                @Override
                public void onSuccess(final VoidResult result) {
                    Info.displayText(I18N.CONSTANTS.saved(), I18N.CONSTANTS.savedChanges());
                }

                @Override
                public final void onFailure(final Throwable caught) {
                    Log.error("error saving profile", caught);
                    MessageBox.alert(I18N.CONSTANTS.serverError(), caught.getMessage(), null);
                }
            });
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return this;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        return true;
    }

    private class ProfileChangeListener implements Listener<FieldEvent> {
        private String fieldname;

        public ProfileChangeListener(String fieldname) {
            this.fieldname = fieldname;
        }

        @Override
        public void handleEvent(FieldEvent fe) {
            // check if we need to save (change-event is also called on init)
            Object modelValue = userProfile.get(fieldname);
            Object fieldValue = fe.getField().getValue();
            if (!Objects.equals(modelValue, fieldValue)) {
                // this eventtype fires before the form->model binding occurs, so we need to
                // set the model-value manually before save..
                userProfile.set(fieldname, fieldValue);
                saveProfile();
            }
        }
    }
    
    private class LocaleChangeListener implements Listener<FieldEvent> {

        @Override
        public void handleEvent(FieldEvent fe) {
            // check if we need to save (change-event is also called on init)
            String modelValue = userProfile.getLocale();
            String newLocale = localeCombo.getMappedValue();
            if (!Objects.equals(modelValue, newLocale)) {
                // this eventtype fires before the form->model binding occurs, so we need to
                // set the model-value manually before save..
                userProfile.setLocale(newLocale);
                saveProfile();
            }
            maybeShowSwitchLocaleLink();
        }
    }

    private void maybeShowSwitchLocaleLink() {
        ApplicationLocale preferredLocale = ApplicationLocale.fromCode(userProfile.getLocale());
        if(!LocaleSwitcher.isCurrent(preferredLocale)) {
            localeLink.setText(I18N.MESSAGES.switchToLanguageNow(preferredLocale.getLocalizedName()));
            localeLink.setHref(LocaleSwitcher.localeUrl(preferredLocale));
            localeLinkField.setVisible(true);
        } else {
            localeLinkField.setVisible(false);
        }
    }

    public static class State implements PageState {
        @Override
        public PageId getPageId() {
            return UserProfilePage.PAGE_ID;
        }

        @Override
        public String serializeAsHistoryToken() {
            return null;
        }

        @Override
        public List<PageId> getEnclosingFrames() {
            return Arrays.asList(UserProfilePage.PAGE_ID);
        }

        @Override
        public Section getSection() {
            return null;
        }

    }

    public static class StateParser implements PageStateParser {
        @Override
        public PageState parse(String token) {
            return new State();
        }
    }
}
