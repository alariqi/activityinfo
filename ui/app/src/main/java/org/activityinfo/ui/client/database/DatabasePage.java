package org.activityinfo.ui.client.database;

import com.google.common.collect.Ordering;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.Place;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.base.avatar.Avatar;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;
import org.activityinfo.ui.client.base.listtable.ListItem;
import org.activityinfo.ui.client.base.listtable.ListTable;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DatabasePage extends Page {

    private final FormStore formStore;
    private final DatabasePlace place;

    public DatabasePage(FormStore formStore, DatabasePlace place) {
        this.formStore = formStore;
        this.place = place;
    }

    @Override
    public VTree render() {
        Observable<Maybe<UserDatabaseMeta>> database = formStore.getDatabase(place.getDatabaseId());

        return AppFrame.render(formStore, new ReactiveComponent(database.transform(d -> {
            switch (d.getState()) {
                case VISIBLE:
                    return page(d.get(), place);
                case FORBIDDEN:
                    return NonIdeal.forbidden();
                default:
                case DELETED:
                case NOT_FOUND:
                    return NonIdeal.notFound();
            }
        })));
    }

    private static VTree page(UserDatabaseMeta database, DatabasePlace databasePlace) {

        if(databasePlace.getFolderId().isPresent()) {
            return folderPage(database, databasePlace.getFolderId().get());
        } else {
            return rootPage(database);
        }
    }

    private static VTree rootPage(UserDatabaseMeta database) {

        return new PageBuilder()
                .padded()
                .avatar(GenericAvatar.DATABASE)
                .heading(database.getLabel())
                .breadcrumbs(Breadcrumb.DATABASES, Breadcrumb.of(database))
                .body(resourceList(database, database.getDatabaseId()))
                .build();
    }

    private static VTree folderPage(UserDatabaseMeta database, ResourceId folderId) {

        Optional<Resource> folder = database.getResource(folderId);
        if(!folder.isPresent()) {
            return NonIdeal.notFound();
        } else {
            return folderPage(database, folder.get());
        }
    }

    private static VTree folderPage(UserDatabaseMeta database, Resource folder) {
        return new PageBuilder()
                .padded()
                .avatar(GenericAvatar.DATABASE)
                .heading(database.getLabel())
                .breadcrumbs(folderBreadcrumbs(database, folder))
                .body(resourceList(database, folder.getId()))
                .build();
    }

    private static List<Breadcrumb> folderBreadcrumbs(UserDatabaseMeta database, Resource folder) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(Breadcrumb.DATABASES);
        breadcrumbs.add(Breadcrumb.of(database));

        while(true) {
            breadcrumbs.add(2,
                    new Breadcrumb(folder.getLabel(),
                            new DatabasePlace(database.getDatabaseId(), folder.getId())));

            Optional<Resource> parent = database.getResource(folder.getParentId());
            if(!parent.isPresent()) {
                break;
            }
            folder = parent.get();
        }
        return breadcrumbs;
    }

    private static VTree resourceList(UserDatabaseMeta database, ResourceId parentId) {

        List<ListItem> items = new ArrayList<>();
        for (Resource resource : database.getResources()) {
            if(resource.getParentId().equals(parentId)) {
                items.add(new ListItem(resource.getId().asString(),
                        resource.getLabel(),
                        place(database, resource),
                        avatar(resource)));
            }
        }

        Collections.sort(items, Ordering.natural().onResultOf(i -> i.getLabel()));

        return new ListTable(items).render();
    }


    private static Place place(UserDatabaseMeta database, Resource resource) {
        if(resource.getType() == ResourceType.FOLDER) {
            return new DatabasePlace(database.getDatabaseId(), resource.getId());
        } else {
            return new TablePlace(resource.getId());
        }
    }

    private static Avatar avatar(Resource resource) {
        if(resource.getType() == ResourceType.FOLDER) {
            return GenericAvatar.FOLDER;
        } else {
            return GenericAvatar.FORM;
        }
    }


}