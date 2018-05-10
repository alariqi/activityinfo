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
package org.activityinfo.ui.client.page.entry;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.Log;
import org.activityinfo.legacy.shared.command.*;
import org.activityinfo.legacy.shared.command.result.VoidResult;
import org.activityinfo.legacy.shared.model.*;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.ClientContext;
import org.activityinfo.ui.client.EventBus;
import org.activityinfo.ui.client.component.importDialog.ImportPresenter;
import org.activityinfo.ui.client.component.importDialog.ImportResultEvent;
import org.activityinfo.ui.client.dispatch.AsyncMonitor;
import org.activityinfo.ui.client.dispatch.Dispatcher;
import org.activityinfo.ui.client.dispatch.ResourceLocator;
import org.activityinfo.ui.client.dispatch.callback.SuccessCallback;
import org.activityinfo.ui.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.ui.client.page.*;
import org.activityinfo.ui.client.page.common.dialog.SaveChangesCallback;
import org.activityinfo.ui.client.page.common.dialog.SavePromptMessageBox;
import org.activityinfo.ui.client.page.common.toolbar.ActionListener;
import org.activityinfo.ui.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.ui.client.page.common.toolbar.UIActions;
import org.activityinfo.ui.client.page.entry.column.DefaultColumnModelProvider;
import org.activityinfo.ui.client.page.entry.form.PrintDataEntryForm;
import org.activityinfo.ui.client.page.entry.form.SiteDialogLauncher;
import org.activityinfo.ui.client.page.entry.grouping.GroupingComboBox;
import org.activityinfo.ui.client.page.entry.place.DataEntryPlace;
import org.activityinfo.ui.client.page.entry.sitehistory.SiteHistoryTab;
import org.activityinfo.ui.client.page.report.ExportDialog;
import org.activityinfo.ui.client.page.report.ExportSitesTask;
import org.activityinfo.ui.client.page.resource.ResourcePage;
import org.activityinfo.ui.client.page.resource.ResourcePlace;
import org.activityinfo.ui.client.style.legacy.icon.IconImageBundle;

import java.util.Objects;
import java.util.Set;

/**
 * This is the container for the DataEntry page.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DataEntryPage extends LayoutContainer implements Page, ActionListener {

    public static final PageId PAGE_ID = new PageId("data-entry");

    private final Dispatcher dispatcher;
    private final EventBus eventBus;

    private GroupingComboBox groupingComboBox;

    private FilterPane filterPane;

    private SiteGridPanel gridPanel;
    private CollapsibleTabPanel tabPanel;

    private DetailTab detailTab;

    private MonthlyReportsPanel monthlyPanel;
    private TabItem monthlyTab;

    private DataEntryPlace currentPlace = new DataEntryPlace();

    private AttachmentsTab attachmentsTab;

    private SiteHistoryTab siteHistoryTab;

    private ActionToolBar toolBar;
    private ResourceLocator resourceLocator;


    @Inject
    public DataEntryPage(final EventBus eventBus,
                         Dispatcher dispatcher, ResourceLocator resourceLocator) {
        this.eventBus = eventBus;
        this.dispatcher = dispatcher;
        this.resourceLocator = resourceLocator;

        setLayout(new BorderLayout());

        addFilterPane();
        addCenter();
    }

    private void addFilterPane() {
        filterPane = new FilterPane(dispatcher);
        BorderLayoutData filterLayout = new BorderLayoutData(LayoutRegion.WEST);
        filterLayout.setCollapsible(true);
        filterLayout.setMargins(new Margins(0, 5, 0, 0));
        filterLayout.setSplit(true);
        add(filterPane, filterLayout);

        filterPane.getSet().addValueChangeHandler(event ->
                eventBus.fireEvent(
                        new NavigationEvent(NavigationHandler.NAVIGATION_REQUESTED,
                            currentPlace.copy().setFilter(event.getValue()))));
    }

    private void addCenter() {
        gridPanel = new SiteGridPanel(dispatcher, new DefaultColumnModelProvider(dispatcher));
        gridPanel.setTopComponent(createToolBar());

        LayoutContainer center = new LayoutContainer();
        center.setLayout(new BorderLayout());

        center.add(gridPanel, new BorderLayoutData(LayoutRegion.CENTER));

        gridPanel.addSelectionChangedListener(new SelectionChangedListener<SiteDTO>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<SiteDTO> se) {
                onSiteSelected(se);
            }
        });
        
        gridPanel.addRowDoubleClickListener(new SelectionChangedListener<SiteDTO>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<SiteDTO> se) {
                editSite(se.getSelectedItem());
            }
        });

        detailTab = new DetailTab(dispatcher);

        monthlyPanel = new MonthlyReportsPanel(dispatcher);
        monthlyTab = new TabItem(I18N.CONSTANTS.monthlyReports());
        monthlyTab.setLayout(new FitLayout());
        monthlyTab.add(monthlyPanel);

        attachmentsTab = new AttachmentsTab(dispatcher, eventBus);

        siteHistoryTab = new SiteHistoryTab(resourceLocator);

        tabPanel = new CollapsibleTabPanel();
        tabPanel.add(detailTab);
        tabPanel.add(monthlyTab);
        tabPanel.add(attachmentsTab);
        tabPanel.add(siteHistoryTab);
        tabPanel.setSelection(detailTab);
        tabPanel.addListener(Events.Select, this::monthlyReportsSelection);
        center.add(tabPanel, tabPanel.getBorderLayoutData());
        onNoSelection();
        add(center, new BorderLayoutData(LayoutRegion.CENTER));
    }

    private void monthlyReportsSelection(BaseEvent tabSelect) {
        monthlyPanel.setActive(tabPanel.getSelectedItem().equals(monthlyTab));
        monthlyPanel.loadIfActive();
    }

    private ActionToolBar createToolBar() {
        toolBar = new ActionToolBar(this);

        groupingComboBox = new GroupingComboBox(dispatcher);
        groupingComboBox.withSelectionListener(event ->
                eventBus.fireEvent(new NavigationEvent(NavigationHandler.NAVIGATION_REQUESTED,
                        currentPlace.copy().setGrouping(groupingComboBox.getGroupingModel()))));

        toolBar.add(new Label(I18N.CONSTANTS.grouping()));
        toolBar.add(groupingComboBox);

        toolBar.addButton(UIActions.ADD, I18N.CONSTANTS.newSite(), IconImageBundle.ICONS.add());
        toolBar.addButton(UIActions.EDIT, I18N.CONSTANTS.edit(), IconImageBundle.ICONS.edit());
        toolBar.addDeleteButton(I18N.CONSTANTS.deleteSite());

        toolBar.add(new SeparatorToolItem());
        toolBar.add(new SeparatorToolItem());

        toolBar.addImportButton();
        toolBar.addExcelExportButton();

        toolBar.addPrintButton();

        return toolBar;
    }

    private void onSiteSelected(final SelectionChangedEvent<SiteDTO> se) {
        if (se.getSelection().isEmpty()) {
            onNoSelection();
        } else {
            final SiteDTO site = se.getSelectedItem();
            int activityId = site.getActivityId();

            dispatcher.execute(new GetActivityForm(activityId), new AsyncCallback<ActivityFormDTO>() {

                @Override
                public void onFailure(Throwable caught) {
                    //
                }

                @Override
                public void onSuccess(ActivityFormDTO activity) {
                    updateSelection(activity, site);
                }
            });
        }
    }

    private void updateSelection(ActivityFormDTO activity, SiteDTO site) {

        boolean permissionToEdit = activity.isAllowedToEdit(site);
        toolBar.setActionEnabled(UIActions.EDIT, permissionToEdit && !site.isLinked());
        toolBar.setActionEnabled(UIActions.DELETE, permissionToEdit && !site.isLinked());
        toolBar.setActionEnabled(UIActions.OPEN_TABLE, site != null);

        detailTab.setSite(site);
        attachmentsTab.setSite(site);
        attachmentsTab.setEnabled(activity.getClassicView());
        if (activity.getReportingFrequency() == ActivityFormDTO.REPORT_MONTHLY) {
            monthlyPanel.load(site);
            monthlyPanel.setReadOnly(!permissionToEdit);
            monthlyTab.setEnabled(true);
        } else {
            monthlyTab.setEnabled(false);
            if (tabPanel.getSelectedItem() == monthlyTab) {
                tabPanel.setSelection(detailTab);
            }
        }
        siteHistoryTab.setSite(site);
    }

    private void onNoSelection() {
        toolBar.setActionEnabled(UIActions.EDIT, false);
        toolBar.setActionEnabled(UIActions.DELETE, false);
        toolBar.setActionEnabled(UIActions.OPEN_TABLE, false);
        monthlyPanel.onNoSelection();
        attachmentsTab.onNoSelection();
        detailTab.onNoSelection();
        siteHistoryTab.onNoSelection();
    }

    @Override
    public void shutdown() {
        //
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
    public void requestToNavigateAway(PageState place, final NavigationCallback callback) {
        if (monthlyPanel.isModified()) {
            final SavePromptMessageBox box = new SavePromptMessageBox();
            box.show(new SaveChangesCallback() {
                @Override
                public void save(AsyncMonitor monitor) {
                    monthlyPanel.save().then(new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            // handled by monitor
                        }

                        @Override
                        public void onSuccess(Void result) {
                            box.hide();
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
        } else {
            callback.onDecided(true);
        }
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        currentPlace = (DataEntryPlace) place;
        if (!currentPlace.getFilter().isRestricted(DimensionType.Activity) &&
            !currentPlace.getFilter().isRestricted(DimensionType.Database)) {

            redirectToFirstActivity();
        } else {
            doNavigate();
        }
        return true;
    }

    private void redirectToFirstActivity() {
        dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

            @Override
            public void onFailure(Throwable caught) {
                //
            }

            @Override
            public void onSuccess(SchemaDTO result) {
                for (UserDatabaseDTO db : result.getDatabases()) {
                    if (!db.getActivities().isEmpty()) {
                        currentPlace.getFilter()
                                    .addRestriction(DimensionType.Activity, db.getActivities().get(0).getId());
                        doNavigate();
                        return;
                    }
                }
            }
        });
    }

    private void doNavigate() {
        Filter filter = currentPlace.getFilter();

        gridPanel.load(currentPlace.getGrouping(), filter);
        groupingComboBox.setFilter(filter);
        filterPane.getSet().applyBaseFilter(filter);
        tabPanel.enable();

        // currently the print form only does one activity
        Set<Integer> activities = filter.getRestrictions(DimensionType.Activity);
        toolBar.enable();
        toolBar.setActionEnabled(UIActions.PRINT, activities.size() == 1);
        toolBar.setActionEnabled(UIActions.IMPORT, activities.size() == 1);

        // adding is also only enabled for one activity, but we have to
        // lookup to see whether it possible for this activity
        toolBar.setActionEnabled(UIActions.ADD, false);

        if (activities.size() == 1) {
            maybeShowTableViewLinkPanel(activities.iterator().next());
        } 
        onNoSelection();
    }

    private void maybeShowTableViewLinkPanel(final Integer activityId) {
        dispatcher.execute(new GetActivityForm(activityId), new AsyncCallback<ActivityFormDTO>() {

            @Override
            public void onFailure(Throwable throwable) {
                // sigh, ignore.
                // will try again when the user navigates
            }

            @Override
            public void onSuccess(ActivityFormDTO form) {
                if (showTableView(form)) {
                    toolBar.disable();
                    // SiteGridPanel holds TableViewLinkPanel
                    tabPanel.disable();
                } else {
                    enableToolbarButtons(form);
                    tabPanel.enable();
                }
            }
        });
    }

    private boolean showTableView(ActivityFormDTO form) {
        // make sure we haven't navigated away by the time the request comes back
        Optional<Integer> currentActivityId = getCurrentActivityId();
        if(!currentActivityId.isPresent() || !Objects.equals(currentActivityId.get(), form.getId())) {
            return false;
        }
        if(form.getReportingFrequency() != ActivityFormDTO.REPORT_ONCE) {
            return false;
        }
        if(form.getClassicView()) {
            return false;
        }
        return true;
    }

    private void enableToolbarButtons(ActivityFormDTO activity) {
        toolBar.enable();
        boolean isAllowed = activity.isEditAllowed();
        toolBar.setActionEnabled(UIActions.ADD, isAllowed);
        toolBar.setActionEnabled(UIActions.IMPORT, isAllowed);
    }

    private void updateEditedSelection(final SiteDTO site) {
        final Integer activityId = site.getActivityId();
        dispatcher.execute(new GetActivityForm(activityId), new AsyncCallback<ActivityFormDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                // Leave unselected
            }

            @Override
            public void onSuccess(ActivityFormDTO result) {
                // make sure we haven't navigated away by the time the request comes back
                Optional<Integer> currentActivityId = getCurrentActivityId();
                if(!currentActivityId.isPresent() || !Objects.equals(currentActivityId.get(), activityId)) {
                    return;
                }
                updateSelection(result, site);
            }
        });
    }

    @Override
    public void onUIAction(String actionId) {
        final Filter filter = currentPlace.getFilter();

        if (UIActions.ADD.equals(actionId)) {

            SiteDialogLauncher formHelper = new SiteDialogLauncher(dispatcher);
            formHelper.addSite(filter, () -> {
                gridPanel.refresh();
                filterPane.getSet().applyBaseFilter(filter);
            });

        } else if (UIActions.EDIT.equals(actionId)) {
            editSite(gridPanel.getSelection());
            
        }else if (UIActions.OPEN_TABLE.equals(actionId)) {
            navigateToNewInterface();
            
        } else if (UIActions.DELETE.equals(actionId)) {
            onDelete();

        } else if (UIActions.PRINT.equals(actionId)) {
            int activityId = filter.getRestrictedCategory(DimensionType.Activity);
            PrintDataEntryForm form = new PrintDataEntryForm(dispatcher);
            form.print(activityId);

        } else if (UIActions.EXPORT.equals(actionId)) {
            ExportDialog dialog = new ExportDialog();
            dialog.start(new ExportSitesTask(dispatcher, filter));

        } else if (UIActions.IMPORT.equals(actionId)) {
            doImport();
        }
    }

    private void editSite(SiteDTO site) {
        SiteDialogLauncher launcher = new SiteDialogLauncher(dispatcher);
        launcher.editSite(site, () -> {
            gridPanel.refresh();
            filterPane.getSet().applyBaseFilter(currentPlace.getFilter());
            updateEditedSelection(gridPanel.getSelection());
        });
    }

    private Optional<Integer> getCurrentActivityId() {
        Filter filter = currentPlace.getFilter();
        Set<Integer> activities = filter.getRestrictions(DimensionType.Activity);
        if(activities.size() == 1) {
            return Optional.of(activities.iterator().next());
        } else {
            return Optional.absent();
        }
    }

    private void navigateToNewInterface() {
        Optional<Integer> activityId = getCurrentActivityId();
        if(activityId.isPresent()) {
            ResourceId formClassId = CuidAdapter.activityFormClass(activityId.get());
            eventBus.fireEvent(new NavigationEvent(
                    NavigationHandler.NAVIGATION_REQUESTED,
                    new ResourcePlace(formClassId, ResourcePage.TABLE_PAGE_ID)));
        }
    }

    private void onDelete() {
        dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

            @Override
            public void onFailure(Throwable caught) {
                showError(caught);
            }

            @Override
            public void onSuccess(SchemaDTO schema) {
                LockedPeriodSet locks = new LockedPeriodSet(schema);
                if (locks.isLocked(gridPanel.getSelection())) {
                    MessageBox.alert(I18N.CONSTANTS.lockedSiteTitle(), I18N.CONSTANTS.siteIsLocked(), null);
                    return;
                }

                MessageBox.confirm(ClientContext.getAppTitle(),
                        I18N.MESSAGES.confirmDeleteSite(),
                        event -> {
                            if (event.getButtonClicked().getItemId().equals(Dialog.YES)) {
                                delete();
                            }
                        });
            }
        });
    }

    private void showError(Throwable caught) {
        MessageBox.alert(I18N.CONSTANTS.serverError(), I18N.CONSTANTS.errorUnexpectedOccured(), null);
        Log.error("Error launching site dialog", caught);
    }

    protected void doImport() {
        final int activityId = currentPlace.getFilter().getRestrictedCategory(DimensionType.Activity);
        ImportPresenter.showPresenter(CuidAdapter.activityFormClass(activityId), resourceLocator)
                       .then(new SuccessCallback<ImportPresenter>() {
                           @Override
                           public void onSuccess(ImportPresenter result) {
                               result.show(ImportPresenter.Mode.MODAL);
                               result.getEventBus().addHandler(ImportResultEvent.TYPE, event -> {
                                   gridPanel.refresh();
                                   filterPane.getSet().applyBaseFilter(currentPlace.getFilter());
                               });
                           }
                       });
    }

    private void delete() {
        dispatcher.execute(new DeleteSite(gridPanel.getSelection().getId()),
                new MaskingAsyncMonitor(this, I18N.CONSTANTS.deleting()),
                new AsyncCallback<VoidResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        // handled by monitor
                    }

                    @Override
                    public void onSuccess(VoidResult result) {
                        gridPanel.refresh();
                        filterPane.getSet().applyBaseFilter(currentPlace.getFilter());
                    }
                });
    }
}
