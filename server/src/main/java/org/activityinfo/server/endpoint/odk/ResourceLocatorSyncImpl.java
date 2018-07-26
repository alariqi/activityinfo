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

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.activityinfo.legacy.shared.AuthenticatedUser;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.FormTreeBuilder;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.server.command.handler.PermissionOracle;
import org.activityinfo.store.hrd.HrdSerialNumberProvider;
import org.activityinfo.store.query.server.ColumnSetBuilder;
import org.activityinfo.store.query.server.Updater;
import org.activityinfo.store.query.shared.NullFormSupervisor;
import org.activityinfo.store.spi.BlobAuthorizer;
import org.activityinfo.store.spi.FormStorage;
import org.activityinfo.store.spi.FormStorageProvider;

import java.util.Collection;
import java.util.List;


public class ResourceLocatorSyncImpl implements ResourceLocatorSync {

    //private static final Logger LOGGER = Logger.getLogger(ResourceLocatorSyncImpl.class.getName());

    private Provider<FormStorageProvider> catalog;
    private Provider<AuthenticatedUser> authenticatedUser;
    private PermissionOracle permissionOracle;
    private BlobAuthorizer blobAuthorizer;

    @Inject
    public ResourceLocatorSyncImpl(Provider<FormStorageProvider> catalog, Provider<AuthenticatedUser> authenticatedUser,
                                   PermissionOracle permissionOracle, BlobAuthorizer blobAuthorizer) {
        this.catalog = catalog;
        this.authenticatedUser = authenticatedUser;
        this.permissionOracle = permissionOracle;
        this.blobAuthorizer = blobAuthorizer;
    }

    @Override
    public FormClass getFormClass(ResourceId formId) {
        return catalog.get().getFormClass(formId);
    }

    @Override
    public FormTree getFormTree(ResourceId formId) {
        FormTreeBuilder builder = new FormTreeBuilder(this);
        return builder.queryTree(formId);
    }

    @Override
    public List<ReferenceChoice> getReferenceChoices(Collection<ResourceId> range) {

        ResourceId formId = Iterables.getOnlyElement(range);
        QueryModel queryModel = new QueryModel(formId);
        queryModel.selectRecordId().as("id");
        queryModel.selectExpr("label").as("label");
        
        ColumnSetBuilder builder = new ColumnSetBuilder(catalog.get(), new NullFormSupervisor());
        ColumnSet columnSet = builder.build(queryModel);

        ColumnView id = columnSet.getColumnView("id");
        ColumnView label = columnSet.getColumnView("label");

        List<ReferenceChoice> choices = Lists.newArrayList();
        for (int i = 0; i < columnSet.getNumRows(); i++) {
            ResourceId choiceId = ResourceId.valueOf(id.getString(i));
            String choiceLabel = label.getString(i);
            
            choices.add(new ReferenceChoice(new RecordRef(formId, choiceId), choiceLabel));
        }

        return choices;
    }

    @Override
    public void persist(FormClass formClass) {
        Optional<FormStorage> form = catalog.get().getForm(formClass.getId());
        if(!form.isPresent()) {
            throw new IllegalArgumentException("no such formId:" + formClass.getId());
        }
        form.get().updateFormClass(formClass);
    }

    @Override
    public ColumnSet query(QueryModel model) {
        ColumnSetBuilder builder = new ColumnSetBuilder(catalog.get(), new NullFormSupervisor());
        return builder.build(model);
    }

    @Override
    public void persist(FormInstance formInstance) {
        Updater updater = new Updater(catalog.get(), authenticatedUser.get().getUserId(), blobAuthorizer,
                new HrdSerialNumberProvider());
        updater.execute(formInstance);
    }
}
