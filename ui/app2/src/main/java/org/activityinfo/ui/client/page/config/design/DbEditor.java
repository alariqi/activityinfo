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
package org.activityinfo.ui.client.page.config.design;

import com.bedatadriven.rebar.async.NullCallback;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.TreeStoreModel;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.common.base.Function;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.i18n.shared.UiConstants;
import org.activityinfo.legacy.shared.Log;
import org.activityinfo.legacy.shared.command.*;
import org.activityinfo.legacy.shared.command.result.BatchResult;
import org.activityinfo.legacy.shared.command.result.CreateResult;
import org.activityinfo.legacy.shared.model.*;
import org.activityinfo.model.job.ExportAuditLog;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldTypeClass;
import org.activityinfo.ui.client.App3;
import org.activityinfo.ui.client.AppEvents;
import org.activityinfo.ui.client.EventBus;
import org.activityinfo.ui.client.dispatch.AsyncMonitor;
import org.activityinfo.ui.client.dispatch.Dispatcher;
import org.activityinfo.ui.client.dispatch.ResourceLocator;
import org.activityinfo.ui.client.dispatch.callback.SuccessCallback;
import org.activityinfo.ui.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.ui.client.dispatch.state.StateProvider;
import org.activityinfo.ui.client.page.*;
import org.activityinfo.ui.client.page.common.dialog.*;
import org.activityinfo.ui.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.ui.client.page.common.toolbar.UIActions;
import org.activityinfo.ui.client.page.config.DbPage;
import org.activityinfo.ui.client.page.config.DbPageState;
import org.activityinfo.ui.client.page.config.design.importer.SchemaImportDialog;
import org.activityinfo.ui.client.page.config.design.importer.SchemaImporterV2;
import org.activityinfo.ui.client.page.config.design.importer.SchemaImporterV3;
import org.activityinfo.ui.client.page.config.form.AbstractDesignForm;
import org.activityinfo.ui.client.page.config.form.FolderForm;
import org.activityinfo.ui.client.page.report.ExportDialog;
import org.activityinfo.ui.client.page.resource.ResourcePage;
import org.activityinfo.ui.client.page.resource.ResourcePlace;
import org.activityinfo.ui.client.style.legacy.icon.IconImageBundle;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Presenter for the Design Page, which enables the user to define UserDatabases
 * and their Activities, Attributes, and Indicators.
 *
 */
public class DbEditor implements DbPage, IsWidget {

    public static final PageId PAGE_ID = new PageId("design");

    private static final Logger LOGGER = Logger.getLogger(DbEditor.class.getName());

    private final EventBus eventBus;
    private final Dispatcher service;
    private ResourceLocator locator;
    private final UiConstants messages;

    private UserDatabaseDTO db;

    private final TreeStore<ModelData> treeStore;
    private final TreePanel<ModelData> tree;

    private final ActionToolBar toolBar;
    private final ContentPanel container;
    private final ContentPanel formContainer;

    private MenuItem newAttributeGroup;
    private MenuItem newAttribute;
    private MenuItem newIndicator;
    private Menu newMenu;
    private AbstractDesignForm currentForm;
    private MenuItem newFolder;
    private MenuItem newActivity;
    private MenuItem newForm;
    private MenuItem newLocationType;


    @Inject
    public DbEditor(EventBus eventBus,
                    Dispatcher service,
                    ResourceLocator locator,
                    StateProvider stateMgr,
                    UiConstants messages) {

        this.eventBus = eventBus;
        this.service = service;
        this.locator = locator;
        this.messages = messages;

        treeStore = new TreeStore<>();
        tree = new TreePanel<>(treeStore);
        tree.setDisplayProperty("name");
        tree.setStateful(true);
        tree.setIconProvider(new DesignIconProvider());
        tree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> event) {
                onSelectionChanged(event.getSelectedItem());
                showForm(event.getSelectedItem());
            }
        });

        TreePanelDragSource source = new TreePanelDragSource(tree);
        source.addDNDListener(new DNDListener() {
            @Override
            public void dragStart(DNDEvent e) {

                ModelData sel = tree.getSelectionModel().getSelectedItem();

                if (sel == null || !hasPermissionToMove(sel)) {
                    e.setCancelled(true);
                    e.getStatus().setStatus(false);
                    return;
                }
                super.dragStart(e);
            }


        });

        TreePanelDropTarget target = new DesignTreeDropTarget(db, tree);
        target.addDNDListener(new DNDListener() {
            @Override
            public void dragDrop(DNDEvent e) {
                onNodeDropped(e.getData());
            }
        });

        toolBar = new ActionToolBar(this::onUIAction);
        initToolBar();

        ContentPanel treeContainer = new ContentPanel();
        treeContainer.setHeaderVisible(false);
        treeContainer.setLayout(new FitLayout());
        treeContainer.add(tree);

        formContainer = new ContentPanel();
        formContainer.setHeadingText("Testing");
        formContainer.setHeaderVisible(false);
        formContainer.setBorders(false);
        formContainer.setFrame(false);
        formContainer.setScrollMode(Style.Scroll.AUTO);

        BorderLayoutData formLayout = new BorderLayoutData(Style.LayoutRegion.EAST);
        formLayout.setSplit(true);
        formLayout.setCollapsible(true);
        formLayout.setSize(385);
        formLayout.setMargins(new Margins(0, 0, 0, 5));

        container = new ContentPanel();
        container.setLayout(new BorderLayout());
        container.setTopComponent(toolBar);
        container.add(treeContainer, new BorderLayoutData(Style.LayoutRegion.CENTER));
        container.add(formContainer, formLayout);
    }

    /**
     * Return true if the user has permission to move the selected item.
     */
    private boolean hasPermissionToMove(ModelData model) {
        if(db.isDesignAllowed() && (model instanceof IndicatorDTO || model instanceof AttributeGroupDTO)) {
            return true;
        }
        if(db.isDatabaseDesignAllowed()) {
            return true;
        }
        return false;
    }

    @Override
    public void go(UserDatabaseDTO db) {

        this.db = db;

        container.setHeadingText(I18N.CONSTANTS.design() + " - "  + db.getName());

        fillStore(messages);

        toolBar.setActionEnabled(UIActions.DELETE, false);
        toolBar.setActionEnabled(UIActions.EDIT, false);
        toolBar.setActionEnabled(UIActions.OPEN_TABLE, false);
        toolBar.setActionEnabled(UIActions.DELETE, db.isDatabaseDesignAllowed());
        toolBar.setActionEnabled(UIActions.IMPORT, db.isDatabaseDesignAllowed());

        newFolder.setEnabled(db.isDatabaseDesignAllowed());
        newActivity.setEnabled(db.isDatabaseDesignAllowed());
        newForm.setEnabled(db.isDatabaseDesignAllowed());
        newLocationType.setEnabled(db.isDatabaseDesignAllowed());
        newMenu.addListener(Events.BeforeShow, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {

                ModelData sel = tree.getSelectionModel().getSelectedItem();
                IsActivityDTO activity = DbEditor.this.getSelectedActivity(sel);

                newAttributeGroup.setEnabled(activity != null && activity.getClassicView());
                newAttribute.setEnabled(activity != null && (sel instanceof AttributeGroupDTO || sel instanceof AttributeDTO) && activity.getClassicView());
                newIndicator.setEnabled(activity != null && activity.getClassicView());
            }
        });
    }

    public void refresh() {
        service.execute(new GetSchema(), new MaskingAsyncMonitor(container, I18N.CONSTANTS.loading()),
                new AsyncCallback<SchemaDTO>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //handled by masking async monitor
                    }

                    @Override
                    public void onSuccess(SchemaDTO result) {
                        db = result.getDatabaseById(db.getId());
                        fillStore(messages);
                        showForm(tree.getSelectionModel().getSelectedItem());
                    }
                });
    }

    @Override
    public void shutdown() {

    }

    private void fillStore(UiConstants messages) {

        treeStore.removeAll();


        for (FolderDTO folderDTO : db.getFolders()) {
            treeStore.add(folderDTO, false);
        }

        for (ActivityDTO activity : db.getActivities()) {

            ActivityDTO activityNode = new ActivityDTO(activity);
            if(activity.getFolder() != null) {
                treeStore.add(activity.getFolder(), activityNode, false);
            } else {
                treeStore.add(activityNode, false);
            }

            if (!activityNode.getClassicView()) {
                continue; // skip indicators and attributes in tree if activity is not classicView=true
            }

            final AttributeGroupFieldGroup attributeFolder = new AttributeGroupFieldGroup(messages.attributes());
            treeStore.add(activityNode, attributeFolder, false);

            final IndicatorFieldGroup indicatorFolder = new IndicatorFieldGroup(messages.indicators());
            treeStore.add(activityNode, indicatorFolder, false);

            service.execute(new GetActivityForm(activity.getId())).then(new SuccessCallback<ActivityFormDTO>() {
                @Override
                public void onSuccess(ActivityFormDTO activityForm) {
                    for (AttributeGroupDTO group : activityForm.getAttributeGroups()) {
                        if (group != null) {
                            AttributeGroupDTO groupNode = new AttributeGroupDTO(group);
                            treeStore.add(attributeFolder, groupNode, false);

                            for (AttributeDTO attribute : group.getAttributes()) {
                                AttributeDTO attributeNode = new AttributeDTO(attribute);
                                treeStore.add(groupNode, attributeNode, false);
                            }
                        }
                    }

                    for (IndicatorGroup group : activityForm.groupIndicators()) {
                        for (IndicatorDTO indicator : group.getIndicators()) {
                            IndicatorDTO indicatorNode = new IndicatorDTO(indicator);
                            treeStore.add(indicatorFolder, indicatorNode, false);
                        }
                    }
                }
            });

        }

        if(db.isDatabaseDesignAllowed()) {
            for (LocationTypeDTO locationType : db.getCountry().getLocationTypes()) {
                if (Objects.equals(locationType.getDatabaseId(), db.getId()) && !locationType.isDeleted()) {
                    treeStore.add(locationType, false);
                }
            }
        }
    }

    @Override
    public boolean navigate(PageState place) {
        return place instanceof DbPageState &&
               place.getPageId().equals(PAGE_ID) &&
               ((DbPageState) place).getDatabaseId() == db.getId();
    }


    private void exportFullDatabase() {
        Window.open("/resources/database/" + db.getId() + "/schema.csv", "_blank", null);
    }

    private void exportFullDatabaseBeta() {
        Window.open("/resources/database/" + db.getId() + "/schema-v3.csv", "_blank", null);
    }
    
    private void exportFormAsXlsForm() {
        Window.open("/resources/form/" + getSelectedFormClassId() + "/form.xls", "_blank", null);
    }

    private void exportAuditLog() {
        ExportDialog dialog = new ExportDialog();
        dialog.start(new ExportJobTask(new ExportAuditLog(db.getId())));
    }


    private void onUIAction(String actionId) {

        if (UIActions.SAVE.equals(actionId)) {
            save();
        } else if (UIActions.DELETE.equals(actionId)) {
            promptDeleteSelection();

        } else if (UIActions.IMPORT.equals(actionId)) {
            SchemaImportDialog dialog = new SchemaImportDialog(
                    new SchemaImporterV2(service, db),
                    new SchemaImporterV3(db.getId(), locator));
            dialog.show().then(new Function<Void, Object>() {
                @Nullable
                @Override
                public Object apply(@Nullable Void input) {
                    refresh();
                    return null;
                }
            });
        } else if(UIActions.EDIT.equals(actionId)) {
            eventBus.fireEvent(new NavigationEvent(
                    NavigationHandler.NAVIGATION_REQUESTED,
                    new ResourcePlace(getSelectedFormClassId(), ResourcePage.DESIGN_PAGE_ID)));

        } else if(UIActions.OPEN_TABLE.equals(actionId)) {
            App3.openNewTable(getSelectedFormClassId());
        }
    }

    private ResourceId getSelectedFormClassId() {
        ModelData selectedItem = tree.getSelectionModel().getSelectedItem();
        if (selectedItem instanceof IsFormClass) {
            IsFormClass formClass = (IsFormClass) selectedItem;
            return formClass.getResourceId();
        } else {
            return getSelectedActivity(selectedItem).getFormClassId();
        }
    }

    private void onNodeDropped(List<TreeStoreModel> data) {

        if(data.isEmpty()) {
            return;
        }

        // update sortOrder and folder membership
        ModelData source = data.get(0).getModel();
        ModelData parent = treeStore.getParent(source);

        LOGGER.info("source = " + source);

        List<ModelData> children = parent == null ? treeStore.getRootItems() : treeStore.getChildren(parent);

        for (int i = 0; i != children.size(); ++i) {
            Record record = treeStore.getRecord(children.get(i));
            record.set("sortOrder", i);
        }

        if(source instanceof ActivityDTO) {
            Record record = treeStore.getRecord(source);
            if(parent instanceof FolderDTO) {
                record.set("folderId", ((FolderDTO) parent).getId());
            } else {
                record.set("folderId", null);
            }
        }
    }

    private void onNew(String entityName) {

        final EntityDTO newEntity;
        ModelData parent;

        ModelData selected = tree.getSelectionModel().getSelectedItem();

        if ("Activity".equals(entityName)) {
            newEntity = new ActivityDTO();
            newEntity.set("databaseId", db.getId());
            newEntity.set("classicView", true);
            newEntity.set("published", Published.NOT_PUBLISHED);
            parent = null;

        } else if("Form".equals(entityName)) {
            newEntity = new ActivityDTO();
            newEntity.set("databaseId", db.getId());
            newEntity.set("classicView", false);
            newEntity.set("reportingFrequency", ActivityFormDTO.REPORT_ONCE);
            newEntity.set("locationTypeId", db.getCountry().getNullLocationType().getId());
            newEntity.set("published", Published.NOT_PUBLISHED);
            parent = null;

        } else if("Folder".equals(entityName)) {
            newEntity = new FolderDTO(db.getId(), null);
            parent = null;

        } else if ("LocationType".equals(entityName)) {
            newEntity = new LocationTypeDTO();
            newEntity.set("databaseId", db.getId());
            parent = null;

        } else if ("AttributeGroup".equals(entityName)) {
            IsActivityDTO activity = findActivityFolder(selected);

            AttributeGroupDTO newAttributeGroup = new AttributeGroupDTO();
            newAttributeGroup.setMultipleAllowed(false);

            newEntity = newAttributeGroup;
            newEntity.set("activityId", activity.getId());
            parent = treeStore.getChild((ModelData) activity, 0);

        } else if ("Attribute".equals(entityName)) {
            AttributeGroupDTO group = findAttributeGroupNode(selected);

            newEntity = new AttributeDTO();
            newEntity.set("attributeGroupId", group.getId());

            parent = group;

        } else if ("Indicator".equals(entityName)) {
            IsActivityDTO activity = findActivityFolder(selected);

            IndicatorDTO newIndicator = new IndicatorDTO();
            newIndicator.setAggregation(IndicatorDTO.AGGREGATE_SUM);
            newIndicator.setType(FieldTypeClass.QUANTITY);

            if (activity instanceof ActivityFormDTO) {
                newIndicator.set("sortOrder", ((ActivityFormDTO)activity).getIndicators().size() + 1);
            }

            newEntity = newIndicator;
            newEntity.set("activityId", activity.getId());

            parent = treeStore.getChild((ModelData) activity, 1);

        } else {
            Log.error("Unsupported entity type.");
            return;
        }

        createEntity(parent, newEntity);
    }

    private void createEntity(final ModelData parent, final EntityDTO newEntity) {
        showNewForm(newEntity, new FormDialogCallback() {
            @Override
            public void onValidated(final FormDialogTether tether) {

                service.execute(new CreateEntity(newEntity), tether, new AsyncCallback<CreateResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(CreateResult result) {
                        newEntity.set("id", result.getNewId());

                        if (parent == null) {
                            treeStore.add(newEntity, false);
                        } else {
                            treeStore.add(parent, newEntity, false);
                        }

                        if (newEntity instanceof IsActivityDTO && ((IsActivityDTO) newEntity).getClassicView()) {
                            treeStore.add(newEntity, new AttributeGroupFieldGroup(messages.attributes()), false);
                            treeStore.add(newEntity, new IndicatorFieldGroup(messages.indicators()), false);
                        }

                        tether.hide();

                        eventBus.fireEvent(AppEvents.SCHEMA_CHANGED);
                    }
                });

            }
        });
    }

    private IsActivityDTO findActivityFolder(ModelData selected) {

        while (!(selected instanceof IsActivityDTO)) {
            selected = treeStore.getParent(selected);
        }

        return (IsActivityDTO) selected;
    }

    private AttributeGroupDTO findAttributeGroupNode(ModelData selected) {
        if (selected instanceof AttributeGroupDTO) {
            return (AttributeGroupDTO) selected;
        }
        if (selected instanceof AttributeDTO) {
            return (AttributeGroupDTO) treeStore.getParent(selected);
        }
        throw new AssertionError("not a valid selection to add an attribute !");

    }

    private void save() {
        // Schedule the save at the end of the event loop so we can
        // handle any blur events from the form

        Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                executeSave(new MaskingAsyncMonitor(DbEditor.this.container, I18N.CONSTANTS.saving()),
                        new NullCallback<>());
            }
        });
    }

    private void executeSave(AsyncMonitor monitor, AsyncCallback<Void> outerCallback) {
        service.execute(unsavedChanges(), monitor, new AsyncCallback<BatchResult>() {
            @Override
            public void onFailure(Throwable caught) {
                outerCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(BatchResult result) {
                treeStore.commitChanges();
                eventBus.fireEvent(AppEvents.SCHEMA_CHANGED);
                outerCallback.onSuccess(null);
            }
        });
    }

    private BatchCommand unsavedChanges() {
        BatchCommand batch = new BatchCommand();

        for (Record record : treeStore.getModifiedRecords()) {
            EntityDTO entity = (EntityDTO) record.getModel();
            Map<String, Object> changes = new HashMap<>();
            for (String property : record.getChanges().keySet()) {
                changes.put(property, entity.get(property));
            }
            batch.add(new UpdateEntity(entity.getEntityName(), entity.getId(), changes));
        }
        return batch;
    }

    private void promptDeleteSelection() {

        ModelData selected = tree.getSelectionModel().getSelectedItem();
        if(!(selected instanceof EntityDTO)) {
            return;
        }

        EntityDTO selectedEntity = (EntityDTO) selected;

        if(selectedEntity instanceof FolderDTO) {
            if(treeStore.getChildCount(selectedEntity) != 0) {
                MessageBox.alert(I18N.CONSTANTS.delete(), I18N.CONSTANTS.folderNotEmpty(), null);
                return;
            }
        }

        SafeHtml message = confirmationMessage(selectedEntity);
        if(message == null) {
            deleteEntity(selectedEntity);
        } else {
            MessageBox.confirm(SafeHtmlUtils.fromString(I18N.CONSTANTS.confirmDeletion()), message, event -> {
                if (event.getButtonClicked().getItemId().equals(Dialog.YES)) {
                    deleteEntity(selectedEntity);
                }
            });
        }
    }

    private void deleteEntity(EntityDTO selectedEntity) {

        BatchCommand batchCommand = unsavedChanges();
        batchCommand.add(new Delete(selectedEntity));

        service.execute(batchCommand, new MaskingAsyncMonitor(container, I18N.CONSTANTS.saving()), new AsyncCallback<BatchResult>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(BatchResult result) {
                treeStore.commitChanges();
                treeStore.remove(selectedEntity);
                eventBus.fireEvent(AppEvents.SCHEMA_CHANGED);
            }
        });
    }

    private SafeHtml confirmationMessage(EntityDTO selectedEntity) {
        if(selectedEntity instanceof ActivityDTO) {
            return I18N.MESSAGES.confirmDeleteForm(selectedEntity.getName());
        } else if(selectedEntity instanceof IndicatorDTO || selectedEntity instanceof AttributeGroupDTO) {
            return SafeHtmlUtils.fromString(I18N.CONSTANTS.deleteFormFieldConfirmation());
        } else {
            return null;
        }
    }


    private void onSelectionChanged(ModelData selectedItem) {
        toolBar.setActionEnabled(UIActions.EDIT, this.db.isDesignAllowed() && canEditWithFormDesigner(selectedItem));
        toolBar.setActionEnabled(UIActions.DELETE, this.db.isDesignAllowed() && selectedItem instanceof EntityDTO);

        // in case of activity enable only if reportingFrequency==once (monthly implementation with subforms is on the way...)
        boolean enableTable = selectedItem instanceof IsFormClass;
        IsActivityDTO selectedActivity = getSelectedActivity(selectedItem);
        if (selectedActivity != null) {
            enableTable = selectedActivity.getReportingFrequency() == ActivityFormDTO.REPORT_ONCE;
        }
        toolBar.setActionEnabled(UIActions.OPEN_TABLE, enableTable);
    }

    private boolean canEditWithFormDesigner(ModelData selectedItem) {
        IsActivityDTO activity = getSelectedActivity(selectedItem);
        return activity != null && !activity.getClassicView();
    }

    private IsActivityDTO getSelectedActivity(ModelData selectedItem) {
        if (selectedItem instanceof IsActivityDTO) {
            return (IsActivityDTO) selectedItem;
        } else if (selectedItem instanceof AttributeGroupFieldGroup ||
                selectedItem instanceof IndicatorFieldGroup ||
                selectedItem instanceof AttributeGroupDTO ||
                selectedItem instanceof IndicatorDTO ||
                selectedItem instanceof LocationTypeDTO ||
                selectedItem instanceof AttributeDTO) {
            return getSelectedActivity(treeStore.getParent(selectedItem));
        }
        return null;
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return container;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        if(treeStore.getModifiedRecords().isEmpty()) {
            callback.onDecided(true);
        } else {
            final SavePromptMessageBox box = new SavePromptMessageBox();
            box.show(new SaveChangesCallback() {
                @Override
                public void save(AsyncMonitor monitor) {
                    executeSave(monitor, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {

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
        }
    }

    @Override
    public String beforeWindowCloses() {
        if(treeStore.getModifiedRecords().isEmpty()) {
            return null;
        } else {
            return I18N.CONSTANTS.unsavedChangesWarning();
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private void initToolBar() {

        toolBar.addSaveButton();

        SelectionListener<MenuEvent> listener = new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                onNew(ce.getItem().getItemId());
            }
        };


        newFolder = new MenuItem(I18N.CONSTANTS.newFolder(), IconImageBundle.ICONS.folder(), listener);
        newFolder.setItemId("Folder");

        newActivity = new MenuItem(I18N.CONSTANTS.newClassicActivity(), IconImageBundle.ICONS.addActivity(), listener);
        newActivity.setItemId("Activity");

        newForm = new MenuItem(I18N.CONSTANTS.newForm(), IconImageBundle.ICONS.form(), listener);
        newForm.setItemId("Form");

        newLocationType = new MenuItem(
                I18N.CONSTANTS.newLocationType(),
                IconImageBundle.ICONS.marker(), listener);
        newLocationType.setItemId("LocationType");

        newAttributeGroup = newMenuItem("AttributeGroup",
                I18N.CONSTANTS.newAttributeGroup(),
                IconImageBundle.ICONS.attribute(),
                listener);

        newAttribute = newMenuItem("Attribute",
                I18N.CONSTANTS.newAttribute(),
                IconImageBundle.ICONS.attribute(),
                listener);

        newIndicator = new MenuItem(I18N.CONSTANTS.newIndicator(),
                IconImageBundle.ICONS.indicator(),
                listener);
        newIndicator.setItemId("Indicator");

        newMenu = new Menu();
        newMenu.add(newFolder);
        newMenu.add(newActivity);
        newMenu.add(newForm);
        newMenu.add(newLocationType);
        newMenu.add(newAttributeGroup);
        newMenu.add(newAttribute);
        newMenu.add(newIndicator);


        Button newButtonMenu = new Button(I18N.CONSTANTS.newText(), IconImageBundle.ICONS.add());
        newButtonMenu.setMenu(newMenu);
        toolBar.add(newButtonMenu);

        toolBar.add(new SeparatorMenuItem());

        toolBar.addButton(UIActions.EDIT, I18N.CONSTANTS.openFormDesigner(), IconImageBundle.ICONS.edit());
        toolBar.addButton(UIActions.OPEN_TABLE, I18N.CONSTANTS.openTable(), IconImageBundle.ICONS.table());
        toolBar.addDeleteButton();

        toolBar.add(new SeparatorToolItem());

        toolBar.addImportButton();

        Menu exportMenu = new Menu();
        exportMenu.add(new MenuItem("Export complete database (classic)", IconImageBundle.ICONS.excel(), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                exportFullDatabase();
            }
        }));
        exportMenu.add(new MenuItem("Export complete database (beta)", IconImageBundle.ICONS.excel(), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                exportFullDatabaseBeta();
            }
        }));
        exportMenu.add(new SeparatorMenuItem());

        exportMenu.add(new MenuItem("Export selected form as XLSForm", IconImageBundle.ICONS.excel(), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                exportFormAsXlsForm();
            }
        }));

        exportMenu.add(new SeparatorMenuItem());

        exportMenu.add(new MenuItem("Export audit log", IconImageBundle.ICONS.excel(), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                exportAuditLog();
            }
        }));

        Button exportButton = new Button(I18N.CONSTANTS.export(), IconImageBundle.ICONS.excel());
        exportButton.setMenu(exportMenu);

        toolBar.add(exportButton);
    }


    private MenuItem newMenuItem(String itemId,
                                 String label,
                                 AbstractImagePrototype icon,
                                 SelectionListener<MenuEvent> listener) {
        final MenuItem newAttribute = new MenuItem(label, icon, listener);
        newAttribute.setItemId(itemId);
        return newAttribute;
    }


    private Class formClassForSelection(ModelData sel) {

        if (sel instanceof IsActivityDTO) {
            return ActivityForm.class;
        } else if (sel instanceof AttributeGroupDTO) {
            return AttributeGroupForm.class;
        } else if (sel instanceof IndicatorDTO) {
            return IndicatorForm.class;
        } else if (sel instanceof AttributeDTO) {
            return AttributeForm.class;
        } else if (sel instanceof LocationTypeDTO) {
            return LocationTypeForm.class;
        } else if (sel instanceof FolderDTO) {
            return FolderForm.class;
        }

        return null;

    }

    private AbstractDesignForm createForm(ModelData sel) {
        if (sel instanceof IsActivityDTO) {
            return new ActivityForm(service, db);
        } else if (sel instanceof FolderDTO) {
            return new FolderForm();
        } else if (sel instanceof AttributeGroupDTO) {
            return new AttributeGroupForm();
        } else if (sel instanceof AttributeDTO) {
            return new AttributeForm();
        } else if (sel instanceof IndicatorDTO) {
            return new IndicatorForm();
        } else if (sel instanceof LocationTypeDTO) {
            return new LocationTypeForm();
        }

        return null;
    }

    private void showForm(ModelData model) {

        // do we have the right form?
        Class formClass = formClassForSelection(model);

        currentForm = null;
        if (formContainer.getItemCount() != 0) {
            currentForm = (AbstractDesignForm) formContainer.getItem(0);
        }

        if (formClass == null) {
            if (currentForm != null) {
                currentForm.getBinding().unbind();
                formContainer.removeAll();
            }
            return;
        } else {

            if (currentForm == null || (!formClass.equals(currentForm.getClass()))) {

                if (currentForm != null) {
                    formContainer.removeAll();
                    currentForm.getBinding().unbind();
                }

                currentForm = createForm(model);
                currentForm.setReadOnly(!isDesignAllowed(model));
                currentForm.setHeaderVisible(false);
                currentForm.setBorders(false);
                currentForm.setFrame(false);
                currentForm.getBinding().setStore(tree.getStore());
                formContainer.add(currentForm);
                formContainer.layout();
            }
        }
        currentForm.getBinding().bind(model);
    }

    private boolean isDesignAllowed(ModelData model) {
        if(db.isDatabaseDesignAllowed()) {
            return true;
        }
        if(db.isDesignAllowed()) {
            return model instanceof IsFormField;
        }

        return false;
    }


    private FormDialogTether showNewForm(EntityDTO entity, FormDialogCallback callback) {

        AbstractDesignForm form = createForm(entity);
        form.getBinding().bind(entity);
        form.getBinding().setStore(tree.getStore());

        for (FieldBinding field : form.getBinding().getBindings()) {
            field.getField().clearInvalid();
        }

        FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setWidth(form.getPreferredDialogWidth());
        dlg.setHeight(form.getPreferredDialogHeight());
        dlg.setScrollMode(Style.Scroll.AUTOY);

        if (entity instanceof IsActivityDTO) {
            dlg.setHeadingText(I18N.CONSTANTS.newActivity());
        } else if (entity instanceof AttributeGroupDTO) {
            dlg.setHeadingText(I18N.CONSTANTS.newAttributeGroup());
        } else if (entity instanceof AttributeDTO) {
            dlg.setHeadingText(I18N.CONSTANTS.newAttribute());
        } else if (entity instanceof IndicatorDTO) {
            dlg.setHeadingText(I18N.CONSTANTS.newIndicator());
        } else if (entity instanceof LocationTypeDTO) {
            dlg.setHeadingText(I18N.CONSTANTS.newLocationType());
        }

        dlg.show(callback);

        return dlg;
    }

}
