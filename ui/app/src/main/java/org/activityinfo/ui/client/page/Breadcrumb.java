package org.activityinfo.ui.client.page;

import com.google.gwt.place.shared.Place;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.PlaceLinks;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.folder.FolderPlace;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Breadcrumb {

    public static final Breadcrumb DATABASES = new Breadcrumb(
            I18N.CONSTANTS.databases(),
            PlaceLinks.toUri(new DatabaseListPlace()));

    private final String label;
    private final SafeUri uri;

    public Breadcrumb(String label, SafeUri uri) {
        this.label = label;
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public SafeUri getUri() {
        return uri;
    }

    public void renderTo(SafeHtmlBuilder html) {
        html.append(PageTemplates.TEMPLATES.breadcrumb(label, uri));
    }

    public VTree render() {
        return H.li(H.link(uri, new VText(label)));
    }


    public static List<Breadcrumb> hierarchy(UserDatabaseMeta database, ResourceId resourceId) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(DATABASES);
        breadcrumbs.add(of(database));

        ResourceId parentId = resourceId;
        while(true) {
            Optional<Resource> parent = database.getResource(parentId);
            if(!parent.isPresent()) {
                break;
            }
            breadcrumbs.add(2, of(parent.get()));

            parentId = parent.get().getParentId();
        }
        return breadcrumbs;
    }

    public static Breadcrumb of(UserDatabaseMeta database) {
        return new Breadcrumb(database.getLabel(), PlaceLinks.toUri(new DatabasePlace(database.getDatabaseId())));
    }

    private static Breadcrumb of(Resource resource) {
        return new Breadcrumb(resource.getLabel(), PlaceLinks.toUri(placeOf(resource)));
    }

    private static Place placeOf(Resource resource) {
        switch (resource.getType()) {
            case DATABASE:
                return new DatabasePlace(resource.getId());
            case FOLDER:
                return new FolderPlace(resource.getId());
            case FORM:
                return new TablePlace(resource.getId());
            default:
                return new DatabaseListPlace();
        }
    }
}
