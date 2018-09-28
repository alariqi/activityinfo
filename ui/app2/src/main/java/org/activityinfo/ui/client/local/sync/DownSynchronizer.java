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

import com.bedatadriven.rebar.async.AsyncCommand;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.activityinfo.i18n.shared.UiConstants;
import org.activityinfo.legacy.shared.Log;
import org.activityinfo.legacy.shared.command.GetSyncRegionUpdates;
import org.activityinfo.legacy.shared.command.GetSyncRegions;
import org.activityinfo.legacy.shared.command.result.SyncRegion;
import org.activityinfo.legacy.shared.command.result.SyncRegionUpdate;
import org.activityinfo.legacy.shared.command.result.SyncRegions;
import org.activityinfo.ui.client.EventBus;
import org.activityinfo.ui.client.dispatch.Dispatcher;
import org.activityinfo.ui.client.dispatch.remote.Remote;
import org.activityinfo.ui.client.local.command.CommandQueue;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Synchronizes the local database by retriving updates from the remote server.
 */
@Singleton
public class DownSynchronizer implements AsyncCommand {

    private final Dispatcher dispatch;
    private final EventBus eventBus;
    private final SqlDatabase conn;
    private final UiConstants uiConstants;

    private ProgressTrackingIterator<SyncRegion> regionIt;

    private AsyncCallback<Void> callback;

    private boolean running = false;

    private SyncRegionTable localVerisonTable;
    private SyncHistoryTable historyTable;
    private CommandQueue commandQueueTable;

    private SynchronizerStats stats = new SynchronizerStats();

    @Inject
    public DownSynchronizer(EventBus eventBus, @Remote Dispatcher dispatch, SqlDatabase conn, UiConstants uiConstants) {
        this.eventBus = eventBus;
        this.conn = conn;
        this.dispatch = dispatch;
        this.uiConstants = uiConstants;

        this.localVerisonTable = new SyncRegionTable(conn);
        this.historyTable = new SyncHistoryTable(conn);
        this.commandQueueTable = new CommandQueue(eventBus, conn);
    }

    @Override
    public void execute(AsyncCallback<Void> callback) {
        this.callback = callback;
        fireStatusEvent(uiConstants.requestingSyncRegions(), 0);
        running = true;
        stats.onStart();
        retrieveSyncRegions();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void retrieveSyncRegions() {
        dispatch.execute(new GetSyncRegions(), new AsyncCallback<SyncRegions>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleException("Error getting sync regions", throwable);
            }

            @Override
            public void onSuccess(SyncRegions syncRegions) {
                DownSynchronizer.this.regionIt = new ProgressTrackingIterator(syncRegions.getList());
                fireStatusEvent("Received sync regions...", 0);
                doNextUpdate();
            }
        });
    }

    private void fireStatusEvent(String task, double percentComplete) {
        Log.info("Synchronizer: " + task + " (" + percentComplete + "%)");
        eventBus.fireEvent(SyncStatusEvent.TYPE, new SyncStatusEvent(task, percentComplete));
    }

    private void doNextUpdate() {
        if (!running) {
            return;
        }
        if (regionIt.hasNext()) {
            final SyncRegion region = regionIt.next();
            updateLocalVersion(region);
        } else {
            onSynchronizationComplete();
        }
    }

    private void updateLocalVersion(final SyncRegion region) {
        localVerisonTable.get(region.getId(), new DefaultCallback<String>() {

            @Override
            public void onSuccess(String localVersion) {
                if (localVersion == null ||
                    region.getCurrentVersion() == null ||
                    !localVersion.equals(region.getCurrentVersion())) {

                    doUpdate(region, localVersion);
                } else {
                    Log.debug("Region " + region.getId() + " is up to date");
                    doNextUpdate();
                }
            }
        });
    }

    private void onSynchronizationComplete() {
        stats.onFinished();
        setLastUpdateTime();
        fireStatusEvent(uiConstants.synchronizationComplete(), 100);
        eventBus.fireEvent(new SyncCompleteEvent(new Date()));
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    private void doUpdate(final SyncRegion region, String localVersion) {

        fireStatusEvent(uiConstants.downSyncProgress(), regionIt.percentComplete());
        Log.info("Synchronizer: Region " + region.getId() + ": localVersion=" + localVersion);

        stats.onRemoteCallStarted();
        dispatch.execute(new GetSyncRegionUpdates(region.getId(), localVersion), new AsyncCallback<SyncRegionUpdate>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleException("GetSyncRegionUpdates for region id " + region.getId() + " failed.", throwable);
            }

            @Override
            public void onSuccess(SyncRegionUpdate update) {
                stats.onRemoteCallFinished();
                persistUpdates(region, update);
            }
        });
    }

    private void persistUpdates(final SyncRegion region, final SyncRegionUpdate update) {
        if (update.getSql() == null) {
            Log.debug("Synchronizer: Region " + region.getId() + " is up to date");
            doNextUpdate();

        } else {
            Log.debug("Synchronizer: persisting updates for region " + region.getId());

            stats.onDbUpdateStarted();
            conn.executeUpdates(update.getSql(), new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    handleException("Synchronizer: Async execution of region " + region.getId() + " failed." +
                                    "\nMessage: " + throwable.getMessage() + ", SQL: " + update.getSql(), throwable);
                }

                @Override
                public void onSuccess(Integer rows) {
                    Log.debug("Synchronizer: updates to region " + region.getId() + " succeeded, " + rows +
                              " row(s) affected");
                    stats.onDbUpdateFinished();
                    updateLocalVersion(region, update);
                }
            });
        }
    }

    private void updateLocalVersion(final SyncRegion region, final SyncRegionUpdate update) {
        localVerisonTable.put(region.getId(), update.getVersion(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                if (!update.isComplete()) {
                    doUpdate(region, update.getVersion());
                } else {
                    doNextUpdate();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    private void handleException(String message, Throwable throwable) {
        Log.error("Synchronizer: " + message, throwable);
        if (callback != null) {
            callback.onFailure(throwable);
        }
    }

    private void setLastUpdateTime() {
        historyTable.update();
    }

    private abstract class DefaultTxCallback extends SqlTransactionCallback {
        @Override
        public final void onError(SqlException e) {
            callback.onFailure(e);
        }
    }

    private abstract class DefaultCallback<T> implements AsyncCallback<T> {
        @Override
        public void onFailure(Throwable caught) {
            callback.onFailure(caught);
        }
    }

    private static final class ProgressTrackingIterator<T> implements Iterator<T> {
        private double total;
        private double completed;
        private Iterator<T> delegateIterator;

        private ProgressTrackingIterator(Collection<T> collection) {
            total = collection.size();
            completed = 0;
            delegateIterator = collection.iterator();
        }

        @Override
        public boolean hasNext() {
            completed++;
            return delegateIterator.hasNext();
        }

        @Override
        public T next() {
            return delegateIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public double percentComplete() {
            return completed / total * 100d;
        }
    }

    public void getLastUpdateTime(AsyncCallback<Date> callback) {
        historyTable.get(callback);
    }
}
