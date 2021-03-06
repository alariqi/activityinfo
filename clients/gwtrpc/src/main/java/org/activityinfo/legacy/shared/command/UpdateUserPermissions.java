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
package org.activityinfo.legacy.shared.command;

import org.activityinfo.legacy.shared.command.result.VoidResult;
import org.activityinfo.legacy.shared.model.UserDatabaseDTO;
import org.activityinfo.legacy.shared.model.UserPermissionDTO;
import org.activityinfo.legacy.shared.validation.Required;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.Valid;

/**
 * Update the permissions of a user to access a given database.
 * <p/>
 * The permissions are updated based on email address as the client will not
 * necessarily know whether a given person already has an account with
 * ActivityInfo. This means that a call to UpdateUserPermissions can result in
 * the creation of a new user account and all that entails, such as an email
 * message, etc.
 */
@JsonAutoDetect(JsonMethod.NONE)
public class UpdateUserPermissions implements MutatingCommand<VoidResult> {

    private int databaseId;
    private UserPermissionDTO model;
    private String host;
    private boolean newUser;

    protected UpdateUserPermissions() {
    }

    public UpdateUserPermissions(UserDatabaseDTO db, UserPermissionDTO model) {
        this(db.getId(), model);
    }

    public UpdateUserPermissions(UserDatabaseDTO db, UserPermissionDTO model, String host) {
        this(db.getId(), model, host);
    }

    public UpdateUserPermissions(int databaseId, UserPermissionDTO model) {
        this.databaseId = databaseId;
        this.model = model;
    }

    public UpdateUserPermissions(int databaseId, UserPermissionDTO model, String host) {
        this.databaseId = databaseId;
        this.model = model;
        this.host = host;
    }

    @Required
    @JsonProperty
    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    @Required
    @Valid
    @JsonProperty
    public UserPermissionDTO getModel() {
        return model;
    }

    public void setModel(UserPermissionDTO model) {
        this.model = model;
    }

    public String getHost() {
        return host;
    }

    public boolean isNewUser() {
        return newUser;
    }

    /**
     * Sets the "newUser" flag to true. This should be set if the user intends to be a new user.
     * An error will be thrown if a user with the same email address already exists in the database.
     * @param newUser
     */
    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "UpdateUserPermissions{" +
                "databaseId=" + databaseId +
                ", model=" + model +
                ", host='" + host + '\'' +
                ", newUser=" + newUser +
                '}';
    }
}
