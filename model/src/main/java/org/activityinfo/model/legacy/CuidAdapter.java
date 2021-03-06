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
package org.activityinfo.model.legacy;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.model.type.ReferenceValue;

/**
 * Provides an adapter between legacy ids, which are either random or sequential 32-bit integers but only
 * guaranteed to be unique within a table, and Collision Resistant Universal Ids (CUIDs) which
 * will serve as the identifiers for all user-created objects.
 */
public class CuidAdapter {

    public static final char REPEATING_DOMAIN = 'b'; // used for subform repeating

    public static final char COUNTRY_DOMAIN = 'F'; // upper case F, avoid conflict with org.activityinfo.model.resource.ResourceId.GENERATED_ID_DOMAIN = 'c'

    public static final char SITE_DOMAIN = 's';

    public static final char ACTIVITY_DOMAIN = 'a';

    public static final char LOCATION_DOMAIN = 'g'; // avoid lower case l !

    public static final char LOCATION_TYPE_DOMAIN = 'L'; // avoid lower case l !

    public static final char PARTNER_DOMAIN = 'p';

    public static final char PARTNER_FORM_CLASS_DOMAIN = 'P';

    public static final char INDICATOR_DOMAIN = 'i';

    public static final char ATTRIBUTE_GROUP_DOMAIN = 'A';

    public static final char ATTRIBUTE_GROUP_FIELD_DOMAIN = 'Q';

    public static final char MONTHLY_REPORT = 'm';

    public static final char MONTHLY_REPORT_FORM_CLASS = 'M';

    public static final char ATTRIBUTE_DOMAIN = 't';

    public static final char DATABASE_DOMAIN = 'd';

    public static final char ADMIN_LEVEL_DOMAIN = 'E';

    public static final char ADMIN_ENTITY_DOMAIN = 'z';

    public static final char PROJECT_CLASS_DOMAIN = 'R';

    public static final char PROJECT_DOMAIN = 'r';

    public static final char ACTIVITY_CATEGORY_DOMAIN = 'C';

    public static final char FOLDER_DOMAIN = 'f';

    public static final char USER_DOMAIN = 'U';

    public static final char LOCK_DOMAIN = 'k';
    
    public static final char TARGET_FORM_CLASS_DOMAIN = 'Q';
    
    public static final char TARGET_INSTANCE_DOMAIN = 'v';
    
    public static final char TARGET_INDICATOR_FIELD_DOMAIN = 'w';
    
    public static final int NAME_FIELD = 1;
    public static final int ADMIN_PARENT_FIELD = 2;
    public static final int CODE_FIELD = 3;
    public static final int AXE_FIELD = 4;
    public static final int GEOMETRY_FIELD = 5;
    public static final int ADMIN_FIELD = 6;
    public static final int PARTNER_FIELD = 7;
    public static final int PROJECT_FIELD = 8;
    public static final int DATE_FIELD = 9;
    public static final int FULL_NAME_FIELD = 10;
    public static final int LOCATION_FIELD = 11;
    public static final int START_DATE_FIELD = 12;
    public static final int END_DATE_FIELD = 13;
    public static final int COMMENT_FIELD = 14;
    public static final int LOCATION_NAME_FIELD = 15;
    public static final int GPS_FIELD = 16;
    public static final int SITE_FIELD = 17;
    public static final int MONTHLY_SUBFORM_FIELD = 18;

    public static final int BLOCK_SIZE = 10;
    public static final String CLASS_FIELD = "_class";

    public static final int[] BUILTIN_FIELDS = new int[] {
            START_DATE_FIELD, END_DATE_FIELD, PARTNER_FIELD, PROJECT_FIELD,
            LOCATION_FIELD, COMMENT_FIELD };


    /**
     * Avoid instance creation.
     */
    private CuidAdapter() {
    }

    public static ResourceId newLegacyFormInstanceId(ResourceId formClassId) {
        if (formClassId != null) {
            return ResourceId.generateSubmissionId(formClassId);
        }
        return ResourceId.generateId();
    }

    public static final int getLegacyIdFromCuid(String cuid) {
        return Integer.parseInt(cuid.substring(1), ResourceId.RADIX);
    }

    public static Optional<Integer> getLegacyIdFromCuidOptional(ResourceId cuid) {
        try {
            return Optional.of(getLegacyIdFromCuid(cuid));
        } catch (NumberFormatException e) {
            return Optional.absent();
        }
    }

    public static final ResourceId cuid(char domain, int id) {
        return ResourceId.valueOf(domain + block(id));
    }

    public static final ResourceId resourceId(char domain, int id) {
        return cuid(domain, id);
    }

    public static int getLegacyIdFromCuid(ResourceId id) {
        if(id.getDomain() == '_' || id.asString().startsWith(ResourceId.GENERATED_ID_DOMAIN + "_")) {
            return 0;
        } else {
            return getLegacyIdFromCuid(id.asString());
        }
    }

    public static boolean isSubformGenerated(ResourceId id) {
        return id.asString().startsWith("c_");
    }

    public static ResourceId partnerRecordId(int partnerId) {
        return cuid(PARTNER_DOMAIN, partnerId);
    }

    public static ReferenceValue partnerRef(int databaseId, int partnerId) {
        return new ReferenceValue(new RecordRef(
                CuidAdapter.partnerFormId(databaseId),
                CuidAdapter.partnerRecordId(partnerId)));
    }

    public static ResourceId projectInstanceId(int projectId) {
        return cuid(PROJECT_DOMAIN, projectId);
    }
    /**
     * @return the {@code FormField}  ResourceId for the Location field of a given Activity {@code FormClass}
     */
    public static ResourceId locationField(int activityId) {
        return field(activityFormClass(activityId), LOCATION_FIELD);
    }

    /**
     * @return the {@code FormClass} ResourceId for a given LocationType
     */
    public static ResourceId locationFormClass(int locationTypeId) {
        return cuid(LOCATION_TYPE_DOMAIN, locationTypeId);
    }

    public static ResourceId locationInstanceId(int locationId) {
        return cuid(LOCATION_DOMAIN, locationId);
    }

    public static ReferenceValue locationRef(ResourceId locationFormId, int locationId) {
        return new ReferenceValue(new RecordRef(locationFormId, CuidAdapter.locationInstanceId(locationId)));
    }

    public static ReferenceValue entityRef(int levelId, int entityId) {
        return new ReferenceValue(new RecordRef(CuidAdapter.adminLevelFormClass(levelId), entity(entityId)));
    }

    public static ResourceId adminLevelFormClass(int adminLevelId) {
        return cuid(ADMIN_LEVEL_DOMAIN, adminLevelId);
    }


    public static ResourceId entity(int adminEntityId) {
        return cuid(ADMIN_ENTITY_DOMAIN, adminEntityId);

    }

    /**
     * Generates a CUID for a FormField in a given previously-built-in FormClass using
     * the FormClass's CUID and a field index.
     *
     * @param classId
     * @param fieldIndex
     * @return
     */
    public static ResourceId field(ResourceId classId, int fieldIndex) {
        return ResourceId.valueOf(classId.asString() + block(fieldIndex));
    }

    /**
     * @return the {@code FormClass} ResourceId for a given Activity
     */
    public static ResourceId activityFormClass(int activityId) {
        return ResourceId.valueOf(ACTIVITY_DOMAIN + block(activityId));
    }


    /**
     * @return the {@code FormClass} ResourceId for a given Activity
     */
    public static ResourceId commentsField(int activityId) {
        //        return new ResourceId(ACTIVITY_DOMAIN + block(activityId) + "C");
        return field(activityFormClass(activityId), COMMENT_FIELD);
    }

    /**
     * @return the {@code FormField} ResourceId for the Partner field of a given Activity {@code FormClass}
     */
    public static ResourceId partnerField(int activityId) {
        return field(activityFormClass(activityId), PARTNER_FIELD);
    }

    public static ResourceId projectField(int activityId) {
        return field(activityFormClass(activityId), PROJECT_FIELD);
    }

    /**
     * @return the {@code FormField} ResourceId for the indicator field within a given
     * Activity {@code FormClass}
     */
    public static ResourceId indicatorField(int indicatorId) {
        return cuid(INDICATOR_DOMAIN, indicatorId);
    }

    public static ResourceId attributeField(int attributeId) {
        return cuid(ATTRIBUTE_DOMAIN, attributeId);
    }

    /**
     * @return the {@code FormField} ResourceId for the field of a given Activity {@code FormClass} that
     * references the given AttributeGroup FormClass
     */
    public static ResourceId attributeGroupField(int attributeGroupId) {
        return cuid(ATTRIBUTE_GROUP_FIELD_DOMAIN, attributeGroupId);
    }

    public static ResourceId folderId(int folderId) {
        return cuid(FOLDER_DOMAIN, folderId);
    }

    public static ResourceId userId(int userId) {
        return cuid(USER_DOMAIN, userId);
    }

    public static ResourceId attributeId(int attributeId) {
        return cuid(ATTRIBUTE_DOMAIN, attributeId);
    }

    /**
     * @param databaseId the id of the database
     * @return the {@code FormClass} ResourceId for a given database's list of partners.
     */
    public static ResourceId partnerFormId(int databaseId) {
        return cuid(PARTNER_FORM_CLASS_DOMAIN, databaseId);
    }

    /**
     * @param databaseId the id of the user database
     * @return the {@code FormClass} ResourceId for a given database's list of projects.
     */
    public static ResourceId projectFormClass(int databaseId) {
        return cuid(PROJECT_CLASS_DOMAIN, databaseId);
    }

    /**
     * @return the {@code FormSection} ResourceId for a given indicator category within an
     * Activity {@code FormClass}
     */
    public static ResourceId activityFormSection(int id, String name) {
        return ResourceId.valueOf(ACTIVITY_DOMAIN + block(id) + block(name.hashCode()));
    }

    private static String block(int id) {
        return Strings.padStart(Integer.toString(id, ResourceId.RADIX), BLOCK_SIZE, '0');
    }

    public static int getBlock(ResourceId resourceId, int blockIndex) {
        int startIndex = 1 + (blockIndex * BLOCK_SIZE);
        String block = resourceId.asString().substring(startIndex, startIndex + BLOCK_SIZE);
        return Integer.parseInt(block, ResourceId.RADIX);
    }

    public static int getBlockSilently(ResourceId resourceId, int blockIndex) {
        try {
            return getBlock(resourceId, blockIndex);
        } catch (Exception e) {
            return -1; // ignore exception
        }
    }

    public static ResourceId databaseId(int databaseId) {
        return cuid(DATABASE_DOMAIN, databaseId);
    }

    public static ResourceId generateLocationCuid() {
        return locationInstanceId(new KeyGenerator().generateInt());
    }

    public static ResourceId generateSiteCuid() {
        return cuid(SITE_DOMAIN, new KeyGenerator().generateInt());
    }

    public static ResourceId countryId(int id) {
        return cuid(COUNTRY_DOMAIN, id);
    }

    public static ResourceId reportingPeriodFormClass(int activityId) {
        return cuid(MONTHLY_REPORT_FORM_CLASS, activityId);
    }

    public static ResourceId lockId(int id) {
        return cuid(LOCK_DOMAIN, id);
    }

    public static ResourceId targetIndicatorField(int indicatorId) {
        return cuid(TARGET_INDICATOR_FIELD_DOMAIN, indicatorId);
    }

    public static ResourceId generateIndicatorId() {
        return indicatorField(new KeyGenerator().generateInt());
    }

    public static ReferenceValue projectRef(int databaseId, int projectId) {
        return new ReferenceValue(new RecordRef(
                CuidAdapter.projectFormClass(databaseId),
                CuidAdapter.projectInstanceId(projectId)));
    }

    public static ResourceId generateActivityId() {
        return CuidAdapter.activityFormClass(new KeyGenerator().generateInt());
    }


    public static FormField partnerField(FormClass formClass) {
        int databaseId = CuidAdapter.getLegacyIdFromCuid(formClass.getDatabaseId());
        ResourceId partnerId = field(formClass.getId(), PARTNER_FIELD);
        FormField formField = new FormField(partnerId);
        formField.setLabel("Partner");
        formField.setType(ReferenceType.single(partnerFormId(databaseId)));
        formField.setRequired(true);
        return formField;
    }

    /**
     * Returns true if the given {@code formId} is a well-formed legacy id. It must start
     * with a single character and have at least 10 digits.
     *
     */
    public static boolean isValidLegacyId(ResourceId formId) {
        Optional<Integer> legacyId = getLegacyIdFromCuidOptional(formId);
        if(legacyId.isPresent()) {
            // Ensure that appropriate number of zeros are present
            ResourceId correctId = cuid(formId.getDomain(), legacyId.get());
            return formId.equals(correctId);
        } else {
            return false;
        }
    }
}
