package org.activityinfo.ui.client.page;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.Place2;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Breadcrumb {

    public static final Breadcrumb DATABASES = new Breadcrumb(
            I18N.CONSTANTS.databases(),
            DatabaseListPlace.INSTANCE.toUri());

    private final String label;
    private final SafeUri uri;
    private final boolean loading;

    private Breadcrumb(String label) {
        this.label = label;
        this.uri = null;
        this.loading = true;
    }

    public Breadcrumb(String label, SafeUri uri) {
        this.label = label;
        this.uri = uri;
        this.loading = false;
    }

    public static Breadcrumb loadingPlaceholder(String dummyText) {
        return new Breadcrumb(dummyText);
    }

    public Breadcrumb(String label, Place2 place) {
        this(label, place.toUri());
    }

    public String getLabel() {
        return label;
    }

    public SafeUri getUri() {
        return uri;
    }

    public boolean isLoading() {
        return loading;
    }

    public void renderTo(SafeHtmlBuilder html) {
        html.append(PageTemplates.TEMPLATES.breadcrumb(label, uri));
    }

    public VTree render() {
        if(loading) {
            return H.li(PropMap.withClasses("breadcrumb--loading"), new VText(label));
        } else {
            return H.li(H.link(uri, new VText(label)));
        }
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
            breadcrumbs.add(2, breadcrumb(database, parent.get()));

            parentId = parent.get().getParentId();
        }
        return breadcrumbs;
    }

    public static Breadcrumb of(UserDatabaseMeta database) {
        return new Breadcrumb(database.getLabel(), new DatabasePlace(database.getDatabaseId()));
    }

    private static Breadcrumb breadcrumb(UserDatabaseMeta database, Resource resource) {
        return new Breadcrumb(resource.getLabel(), placeOf(database, resource));
    }


    private static Place2 placeOf(UserDatabaseMeta database, Resource resource) {
        switch (resource.getType()) {
            case DATABASE:
                return new DatabasePlace(resource.getId());
            case FOLDER:
                return new DatabasePlace(database.getDatabaseId(), resource.getId());
            case FORM:
                return new TablePlace(resource.getId());
            default:
                return DatabaseListPlace.INSTANCE;
        }
    }
}
