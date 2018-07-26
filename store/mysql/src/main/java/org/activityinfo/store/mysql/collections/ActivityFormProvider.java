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
package org.activityinfo.store.mysql.collections;

import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.mysql.cursor.QueryExecutor;
import org.activityinfo.store.mysql.mapping.ActivityTableMappingBuilder;
import org.activityinfo.store.mysql.mapping.TableMapping;
import org.activityinfo.store.mysql.metadata.Activity;
import org.activityinfo.store.mysql.metadata.ActivityLoader;
import org.activityinfo.store.spi.FormNotFoundException;
import org.activityinfo.store.spi.FormStorage;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import static java.util.Collections.singleton;


public class ActivityFormProvider implements FormProvider {
    
    private static final Logger LOGGER = Logger.getLogger(ActivityFormProvider.class.getName());

    /**
     * Caches activities within a session to avoid fetching Activity for both Site and ReportingFrequency table
     */
    private ActivityLoader activityLoader;

    public ActivityFormProvider(ActivityLoader activityLoader) {
        this.activityLoader = activityLoader;
    }

    @Override
    public boolean accept(ResourceId formId) {
        return (formId.getDomain() == CuidAdapter.ACTIVITY_DOMAIN ||
                formId.getDomain() == CuidAdapter.MONTHLY_REPORT_FORM_CLASS) &&
                CuidAdapter.isValidLegacyId(formId);
    }

    @Override
    public FormStorage openForm(QueryExecutor executor, ResourceId formId) throws SQLException {
        int activityId = CuidAdapter.getLegacyIdFromCuid(formId);
        Map<Integer, Activity> map = activityLoader.load(singleton(activityId));
        
        Activity activity = map.get(activityId);
        if(activity == null) {
            throw new FormNotFoundException(formId);
        }
        return new SiteFormStorage(activity, buildMapping(activity, formId), executor,
                activityLoader.getPermissionCache(), activityLoader);
    }
    
    
    private TableMapping buildMapping(Activity activity, ResourceId collectionId) {
        if(collectionId.getDomain() == CuidAdapter.ACTIVITY_DOMAIN) {
            return ActivityTableMappingBuilder.site(activity).build();
            
        } else {
            return ActivityTableMappingBuilder.reportingPeriod(activity).build();
        }
    }

    @Override
    public Map<ResourceId, FormStorage> openForms(QueryExecutor executor, Set<ResourceId> formIds) throws SQLException {
        Set<Integer> activityIds = new HashSet<>();
        for (ResourceId collectionId : formIds) {
            if(accept(collectionId)) {
                activityIds.add(CuidAdapter.getLegacyIdFromCuid(collectionId));
            }
        }
        if (activityIds.isEmpty()) {
            return Collections.emptyMap();
        
        } else {
            Map<Integer, Activity> activityMap = activityLoader.load(activityIds);
            Map<ResourceId, FormStorage> collectionMap = new HashMap<>();

            for (ResourceId collectionId : formIds) {
                if (accept(collectionId)) {
                    Activity activity = activityMap.get(CuidAdapter.getLegacyIdFromCuid(collectionId));
                    if (activity != null) {
                        collectionMap.put(collectionId,
                                new SiteFormStorage(activity, buildMapping(activity, collectionId), executor,
                                        activityLoader.getPermissionCache(), activityLoader));
                    }
                }
            }
            return collectionMap;
        }
    }
}
