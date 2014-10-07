package org.activityinfo.service.store;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.legacy.InstanceQuery;
import org.activityinfo.model.legacy.Projection;
import org.activityinfo.model.legacy.QueryResult;
import org.activityinfo.model.legacy.criteria.Criteria;
import org.activityinfo.model.resource.*;
import org.activityinfo.model.system.ApplicationClassProvider;
import org.activityinfo.model.table.InstanceLabelTable;
import org.activityinfo.model.table.TableData;
import org.activityinfo.model.table.TableModel;
import org.activityinfo.promise.Promise;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Exposes a legacy {@code Dispatcher} implementation as new {@code ResourceLocator}
 */
public class ResourceLocatorAdaptor implements ResourceLocator {

    private final ApplicationClassProvider systemClassProvider = new ApplicationClassProvider();


    private final RemoteStoreService store;

    private final ProjectionAdapter projectionAdapter;

    public ResourceLocatorAdaptor(RemoteStoreService tableService) {
        this.store = tableService;
        this.projectionAdapter = new ProjectionAdapter(tableService);
    }

    @Override
    public Promise<FormClass> getFormClass(ResourceId classId) {
        if(classId.asString().startsWith("_")) {
            return Promise.resolved(systemClassProvider.get(classId));
        } else {
            return store.get(classId).then(new Function<UserResource, FormClass>() {
                @Nullable
                @Override
                public FormClass apply(@Nullable UserResource input) {
                    return FormClass.fromResource(input.getResource());
                }
            });
        }
    }

    @Override
    public Promise<FormInstance> getFormInstance(ResourceId instanceId) {
        return store.get(instanceId).then(new Function<UserResource, FormInstance>() {
            @Nullable
            @Override
            public FormInstance apply(@Nullable UserResource input) {
                return FormInstance.fromResource(input.getResource());
            }
        });
    }

    @Override
    public Promise<List<UserResource>> get(Set<ResourceId> resourceIds) {
        return Promise.map(resourceIds, new Function<ResourceId, Promise<UserResource>>() {
            @Override
            public Promise<UserResource> apply(ResourceId input) {
                return store.get(input);
            }
        });
    }

    @Override
    public Promise<TableData> queryTable(TableModel tableModel) {
        return store.queryTable(tableModel);
    }

    @Override
    public Promise<List<ResourceNode>> getRoots() {
        return store.getWorkspaces();
    }

    @Override
    public Promise<Void> persist(IsResource resource) {
        return store.put(resource.asResource()).thenDiscardResult();
    }

    @Override
    public Promise<Void> persist(List<? extends IsResource> resources) {
        final List<Promise<Void>> promises = Lists.newArrayList();
        if (resources != null && !resources.isEmpty()) {
            for (final IsResource resource : resources) {
                promises.add(persist(resource));
            }
        }
        return Promise.waitAll(promises);
    }

    @Override
    public Promise<List<FormInstance>> queryInstances(Criteria criteria) {
        throw new UnsupportedOperationException("deprecated");
    }

    @Override
    public Promise<QueryResult> queryProjection(InstanceQuery query) {
        return projectionAdapter.query(query);
    }

    @Override
    public Promise<FolderProjection> getTree(final ResourceId rootId) {
        return store.getFolder(rootId);
    }

    @Override
    public Promise<List<Projection>> query(InstanceQuery query) {
        return projectionAdapter.query(query).then(new Function<QueryResult, List<Projection>>() {
            @Override
            public List<Projection> apply(QueryResult input) {
                return input.getProjections();
            }
        });
    }

    @Override
    public Promise<Void> remove(Collection<ResourceId> resources) {
        List<Promise<?>> promises = Lists.newArrayList();
        for (ResourceId resource : resources) {
            promises.add(store.remove(resource));
        }
        return Promise.waitAll(promises);
    }

    @Override
    public Promise<InstanceLabelTable> queryFormList() {
        throw new UnsupportedOperationException();
    }
}