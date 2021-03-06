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

import org.activityinfo.legacy.shared.command.result.UserResult;

/**
 * Queries the list of users authorized to access a given
 * {@link org.activityinfo.server.database.hibernate.entity.UserDatabase}
 * <p/>
 * The resulting {@link org.activityinfo.legacy.shared.model.UserPermissionDTO} are a
 * projection of the UserLogin, UserPermission, and Partner tables.
 */
public class GetUsers extends PagingGetCommand<UserResult> {

    private int databaseId;

    private GetUsers() {
        // required
    }

    public GetUsers(int databaseId) {
        super();
        this.databaseId = databaseId;
    }

    /**
     * Gets the id of the database for which to query the list of
     * UserPermissions.
     *
     * @return the id of the database for which to query the list of authorized
     * users.
     */
    public int getDatabaseId() {
        return databaseId;
    }

    /**
     * Sets the id of the database for which toquery the list of
     * UserPermissions.
     *
     * @param databaseId
     */
    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }
}
