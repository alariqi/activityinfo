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
package org.activityinfo.ui.client.local.sync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.legacy.shared.command.Command;

import java.util.Date;

/**
 * Decouples the actual offline implementation from the manager so we can stick
 * all the offline code in a separate js split.
 */
public interface Synchronizer {
    void install(AsyncCallback<Void> callback);

    /**
     * @return the date of the last successful synchronization to the client
     */
    void getLastSyncTime(AsyncCallback<Date> callback);

    /**
     * Conducts sanity checks to be sure that we are really prepared to go
     * offline. (It's possible that the Offline "state" flag was mis-set in the
     * past.)
     *
     * @return true if we are ready
     */
    void validateOfflineInstalled(AsyncCallback<Void> callback);

    void synchronize();

    // TODO: move to separate interface
    void execute(Command command, AsyncCallback callback);

}
