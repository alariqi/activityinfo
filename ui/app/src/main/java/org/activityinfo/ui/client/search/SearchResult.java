package org.activityinfo.ui.client.search;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;

public class SearchResult {
    private ResourceId id;
    private ResourceId databaseId;
    private String label;
    private ResourceType resourceType;
    private String databaseName;

    public SearchResult(UserDatabaseMeta database) {
        this.id = database.getDatabaseId();
        this.databaseId = database.getDatabaseId();
        this.resourceType = ResourceType.DATABASE;
        this.label = database.getLabel();
        this.databaseName = database.getLabel();
    }

    public SearchResult(UserDatabaseMeta database, Resource resource) {
        this.id = resource.getId();
        this.databaseId = database.getDatabaseId();
        this.resourceType = resource.getType();
        this.label = resource.getLabel();
        this.databaseName = database.getLabel();
    }

    public ResourceId getId() {
        return id;
    }

    public ResourceId getDatabaseId() {
        return databaseId;
    }

    public String getKey() {
        return id.asString();
    }

    public String getLabel() {
        return label;
    }

    public String getDatabase() {
        return databaseName;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * @return the CSS class name to use for this result
     */
    public String getTypeClassName() {
        return resourceType.name().toLowerCase();
    }

    public SafeHtml getTypeLabel() {
        switch (resourceType) {
            case DATABASE:
                return SearchTemplates.TEMPLATES.databaseLabel(I18N.CONSTANTS.database());
            case FORM:
                return I18N.MESSAGES.formDatabaseLabel(databaseName);
            case FOLDER:
                return I18N.MESSAGES.folderDatabaseLabel(databaseName);
        }
        return SafeHtmlUtils.EMPTY_SAFE_HTML;
    }

    @Override
    public String toString() {
        return label;
    }

}
