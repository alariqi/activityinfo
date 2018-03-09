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
package org.activityinfo.server.endpoint.odk;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.ReferenceType;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

public class ItemSetBuilder {

    private final ResourceLocatorSync locator;
    private final Provider<EntityManager> entityManager;

    @Inject
    public ItemSetBuilder(ResourceLocatorSync locator, Provider<EntityManager> entityManager) {
        this.locator = locator;
        this.entityManager = entityManager;
    }

    public StreamingOutput build(ResourceId formClassId) throws IOException {

        final Set<ResourceId> rangeClassIds = findRanges(formClassId);

        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {

                ItemSetWriter writer = new ItemSetWriter(output);

                // We need at least one item set or ODK will crash
                writer.writeItem("__dummy", "dummy", "dummy");

                // Write out real item sets
                for(ResourceId formClassId : rangeClassIds) {
                    writeInstances(formClassId, writer);
                }
                writer.flush();
            }
        };
    }

    private Set<ResourceId> findRanges(ResourceId formClassId) {

        FormClass formClass = locator.getFormClass(formClassId);

        Set<ResourceId> rangeClassIds = Sets.newHashSet();
        for(FormField field : formClass.getFields()) {
            if(field.getType() instanceof ReferenceType) {
                rangeClassIds.addAll(((ReferenceType) field.getType()).getRange());
            }
        }
        return rangeClassIds;
    }

    private void writeInstances(ResourceId formClassId, ItemSetWriter writer) throws IOException {

        String listName = formClassId.asString();

        if(formClassId.getDomain() == CuidAdapter.LOCATION_TYPE_DOMAIN) {
            List<Tuple> instances = entityManager.get().createQuery(
                    "select g.id, g.name from Location g where g.locationType.id = :id", Tuple.class)
                    .setParameter("id", CuidAdapter.getLegacyIdFromCuid(formClassId))
                    .getResultList();

            writer.writeItem(listName, "NEW", "[NEW]");

            for(Tuple instance : instances) {
                String id = CuidAdapter.locationInstanceId(instance.get(0, Integer.class)).asString();
                String label = instance.get(1, String.class);
                writer.writeItem(listName, id, label);
            }
        }
    }

}
