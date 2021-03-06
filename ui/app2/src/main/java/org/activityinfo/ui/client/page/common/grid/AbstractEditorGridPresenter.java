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
package org.activityinfo.ui.client.page.common.grid;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.command.Command;
import org.activityinfo.legacy.shared.command.result.BatchResult;
import org.activityinfo.ui.client.EventBus;
import org.activityinfo.ui.client.dispatch.AsyncMonitor;
import org.activityinfo.ui.client.dispatch.Dispatcher;
import org.activityinfo.ui.client.dispatch.callback.SuccessCallback;
import org.activityinfo.ui.client.dispatch.loader.CommandLoadEvent;
import org.activityinfo.ui.client.dispatch.state.StateProvider;
import org.activityinfo.ui.client.page.NavigationCallback;
import org.activityinfo.ui.client.page.PageState;
import org.activityinfo.ui.client.page.common.dialog.SaveChangesCallback;
import org.activityinfo.ui.client.page.common.dialog.SavePromptMessageBox;
import org.activityinfo.ui.client.page.common.toolbar.UIActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEditorGridPresenter<M extends ModelData> extends AbstractGridPresenter<M> {

    private GridView view;
    private Dispatcher service;
    private boolean isDirty = false;

    protected AbstractEditorGridPresenter(EventBus eventBus,
                                          Dispatcher service,
                                          StateProvider stateMgr,
                                          GridView view) {
        super(eventBus, stateMgr, view);
        this.view = view;
        this.service = service;
    }

    @Override
    protected void initListeners(Store store, Loader loader) {
        super.initListeners(store, loader);

        store.addListener(Store.Update, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent be) {
                boolean isDirtyNow = be.getStore().getModifiedRecords().size() != 0;
                if (isDirty != isDirtyNow) {
                    isDirty = isDirtyNow;
                    onDirtyFlagChanged(isDirty);
                }
            }
        });
    }

    @Override
    public void onUIAction(String actionId) {
        super.onUIAction(actionId);

        if (UIActions.SAVE.equals(actionId)) {
            onSave();
        } else if (UIActions.DISCARD_CHANGES.equals(actionId)) {
            getStore().rejectChanges();
        }

    }

    protected abstract Command createSaveCommand();

    public abstract Store getStore();

    /**
     * Returns the list of modified records. The default implementation simply
     * calls <code>getStore().getModifiedRecords()</code> but should be
     * overriden for EditorTree which doesn't appear to track which records have
     * been modified.
     *
     * @return The list of modified records
     */
    public List<Record> getModifiedRecords() {
        return getStore().getModifiedRecords();
    }

    /**
     * Responds to an explict user action to save
     */
    public void onSave() {

        if (!isValid()) {
            return;
        }

        service.execute(createSaveCommand(), view.getSavingMonitor(), new AsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                // let the monitor handle failure, we're not
                // expecting any exceptions
            }

            @Override
            public void onSuccess(Object result) {
                getStore().commitChanges();

                if (AbstractEditorGridPresenter.this.view.getToolBar() != null) {
                    AbstractEditorGridPresenter.this.view.getToolBar().setDirty(false);
                }
                onSaved();
            }
        });
    }

    private boolean isValid() {
        return true;
    }

    /**
     * The grid is about to be refreshed, if there are modifications then the
     * save command needs to be included in the call to the server
     *
     * @param le Load Event
     */
    @Override
    protected void onBeforeLoad(CommandLoadEvent le) {
        if (getModifiedRecords().size() != 0) {
            le.addCommandToBatch(createSaveCommand());
        }
    }

    /*
     * The user has chosen to navigate away from this page We will automatically
     * try to save any unsaved changes, but if it fails, we give the user a
     * choice between retrying and and discarding changes
     */

    @Override
    public void requestToNavigateAway(PageState place, final NavigationCallback callback) {

        if (getModifiedRecords().size() == 0) {
            callback.onDecided(true);
        } else {
            final SavePromptMessageBox box = new SavePromptMessageBox();
            box.show(new SaveChangesCallback() {
                @Override
                public void save(AsyncMonitor monitor) {
                    service.execute(createSaveCommand(), view.getSavingMonitor(), new SuccessCallback<BatchResult>() {
                        @Override
                        public void onSuccess(BatchResult result) {
                            box.hide();
                            getStore().commitChanges();
                            callback.onDecided(true);
                        }
                    });
                }

                @Override
                public void cancel() {
                    box.hide();
                    callback.onDecided(false);
                }

                @Override
                public void discard() {
                    box.hide();
                    callback.onDecided(true);
                }
            });
        }
    }

    @Override
    public String beforeWindowCloses() {
        if (getModifiedRecords().size() == 0) {
            return null;
        } else {
            return I18N.CONSTANTS.unsavedChangesWarning();
        }
    }

    @Override
    public void onDirtyFlagChanged(boolean isDirty) {
        view.setActionEnabled(UIActions.SAVE, isDirty);
    }

    protected Map<String, Object> getChangedProperties(Record record) {
        Map<String, Object> changes = new HashMap<String, Object>();

        for (String property : record.getChanges().keySet()) {
            changes.put(property, record.get(property));
        }
        return changes;
    }

    protected void onSaved() {

    }

}
