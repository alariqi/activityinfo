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

import org.activityinfo.legacy.shared.Log;

import java.util.Date;

/**
 * Logging utility to track where time is spent during sychronization. Should
 * compile out when logging is turned off.
 */
public class SynchronizerStats {

    private double timeStarted;
    private double timeWaitingForServer = 0;
    private double timeWaitingForLocalDatabase = 0;

    public void onStart() {
        if (Log.isDebugEnabled()) {
            timeStarted = now();
        }
    }

    public void onRemoteCallStarted() {
        if (Log.isDebugEnabled()) {
            timeWaitingForServer -= now();
        }
    }

    public void onRemoteCallFinished() {
        if (Log.isDebugEnabled()) {
            timeWaitingForServer += now();
        }
    }

    public void onDbUpdateStarted() {
        if (Log.isDebugEnabled()) {
            timeWaitingForLocalDatabase -= now();
        }
    }

    public void onDbUpdateFinished() {
        if (Log.isDebugEnabled()) {
            timeWaitingForLocalDatabase += now();
        }
    }

    public void onFinished() {
        if (Log.isDebugEnabled()) {
            double totalLength = now() - timeStarted;
            Log.debug("Sync stats: total time: " + totalLength / 1000 + " s");
            Log.debug("Sync stats: time waiting for remote server: " + timeWaitingForServer / 1000d + " s");
            Log.debug("Sync stats: time waiting for local database updates: " + timeWaitingForLocalDatabase / 1000d +
                      " s");
        }
    }

    private double now() {
        return new Date().getTime();
    }

}
