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
package org.activityinfo.ui.client.dispatch.remote.cache;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.activityinfo.legacy.shared.command.GetAdminEntities;
import org.activityinfo.legacy.shared.command.result.AdminEntityResult;
import org.activityinfo.legacy.shared.command.result.CommandResult;
import org.activityinfo.ui.client.dispatch.CommandCache;
import org.activityinfo.ui.client.dispatch.DispatchEventSource;
import org.activityinfo.ui.client.dispatch.DispatchListener;

/**
 * Caches the results of
 * {@link org.activityinfo.legacy.shared.command.GetAdminEntities}
 */
@Singleton
public class AdminEntityCache extends AbstractCache implements CommandCache<GetAdminEntities>,
        DispatchListener<GetAdminEntities> {

    @Inject
    public AdminEntityCache(DispatchEventSource connection) {
        connection.registerProxy(GetAdminEntities.class, this);
        connection.registerListener(GetAdminEntities.class, this);
    }

    @Override
    public CacheResult<AdminEntityResult> maybeExecute(GetAdminEntities command) {

        if (command.getFilter() != null) {
            return CacheResult.couldNotExecute();
        }

        AdminEntityResult result = (AdminEntityResult) fetch(command);
        if (result == null) {
            return CacheResult.couldNotExecute();
        } else {
            return new CacheResult<AdminEntityResult>(new AdminEntityResult(result));
        }
    }

    @Override
    public void beforeDispatched(GetAdminEntities command) {

    }

    @Override
    public void onFailure(GetAdminEntities command, Throwable caught) {

    }

    @Override
    public void onSuccess(GetAdminEntities command, CommandResult result) {

        if (command.getFilter() == null) {
            cache(command, new AdminEntityResult((AdminEntityResult) result));
        }
    }

}
