package org.activityinfo.ui.client.header;

import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.PlaceLinks;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BreadcrumbViewModel {

    public static class CrumbModel {
        private ResourceId id;
        private String label;
        private SafeUri href;
        private ResourceType type;

        private CrumbModel(UserDatabaseMeta database) {
            this.id = database.getDatabaseId();
            this.label = database.getLabel();
            this.href = PlaceLinks.toUri(new DatabasePlace(database.getDatabaseId()));
            this.type = ResourceType.DATABASE;
        }

        public CrumbModel(Resource resource) {
            this.id = resource.getId();
            this.label = resource.getLabel();
            this.type = resource.getType();
        }

        public ResourceId getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public SafeUri getHref() {
            return href;
        }

        public ResourceType getType() {
            return type;
        }
    }

    private List<CrumbModel> crumbs;

    private BreadcrumbViewModel(List<CrumbModel> crumbs) {
        this.crumbs = crumbs;
    }

    public List<CrumbModel> getCrumbs() {
        return crumbs;
    }

    public static BreadcrumbViewModel forDatabase(UserDatabaseMeta database) {
        return new BreadcrumbViewModel(Collections.singletonList(new CrumbModel(database)));
    }

    public static BreadcrumbViewModel forForm(UserDatabaseMeta database, ResourceId formId) {
        List<CrumbModel> crumbs = new ArrayList<>();
        crumbs.add(new CrumbModel(database));
        collectParentsAndSelf(crumbs, database, formId);
        return new BreadcrumbViewModel(crumbs);
    }

    public static Observable<BreadcrumbViewModel> forForm(FormStore formStore, ResourceId formId) {
        return formStore.getFormMetadata(formId)
                .transform(f -> f.getSchema().getDatabaseId())
                .join(databaseId -> formStore.getDatabase(databaseId))
                .transform(database -> forForm(database, formId));
    }

    private static void collectParentsAndSelf(List<CrumbModel> crumbs, UserDatabaseMeta database, ResourceId id) {

        // Have we reached the top of the hierarchy?
        if(id.equals(database.getDatabaseId())) {
            return;
        }

        Optional<Resource> resource = database.getResource(id);
        if(!resource.isPresent()) {
            return;
        }

        collectParentsAndSelf(crumbs, database, resource.get());

    }

    private static void collectParentsAndSelf(List<CrumbModel> crumbs, UserDatabaseMeta database, Resource resource) {

        collectParentsAndSelf(crumbs, database, resource.getParentId());

        crumbs.add(new CrumbModel(resource));
    }
}
