package org.activityinfo.store.server;


import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.sun.jersey.api.NotFoundException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import org.activityinfo.io.xlsform.XlsFormBuilder;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.*;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.FormTreeBuilder;
import org.activityinfo.model.formTree.FormTreePrettyPrinter;
import org.activityinfo.model.formTree.JsonFormTreeBuilder;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.geo.GeoAreaType;
import org.activityinfo.store.query.output.ColumnJsonWriter;
import org.activityinfo.store.query.output.RowBasedJsonWriter;
import org.activityinfo.store.query.server.InvalidUpdateException;
import org.activityinfo.store.query.server.PermissionsEnforcer;
import org.activityinfo.store.spi.FormStorage;
import org.activityinfo.store.spi.VersionedFormStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static org.activityinfo.model.resource.ResourceId.valueOf;

public class FormResource {

    public static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final Logger LOGGER = Logger.getLogger(FormResource.class.getName());

    private final ApiBackend backend;
    private final ResourceId formId;

    public FormResource(ApiBackend backend, ResourceId formId) {
        this.backend = backend;
        this.formId = formId;
    }

    @GET
    @NoCache
    @Produces(JSON_CONTENT_TYPE)
    public FormMetadata getMetadataResponse(@QueryParam("localVersion") Long localVersion) {

        Optional<FormStorage> storage = backend.getStorage().getForm(formId);
        if(!storage.isPresent()) {
            return FormMetadata.notFound(formId);
        }
        FormPermissions permissions = backend.getFormSupervisor().getFormPermissions(formId);
        if(!permissions.isVisible()) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);

        } else {
            return FormMetadata.of(
                    storage.get().cacheVersion(),
                    storage.get().getFormClass(),
                    permissions);
        }
    }

    /**
     *
     * @return this form's {@link org.activityinfo.model.form.FormClass}
     */
    @GET
    @NoCache
    @Path("schema")
    @Produces(JSON_CONTENT_TYPE)
    public FormClass getFormSchema() {
        return assertVisible(formId).getFormClass();
    }

    @POST
    @Path("schema")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUpdatedFormSchema(FormClass updatedFormClass) {

        return updateFormSchema(updatedFormClass);
    }

    @PUT
    @Path("schema")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFormSchema(FormClass updatedFormClass) {

        // Check first to see if this collection exists
        Optional<FormStorage> form = backend.getStorage().getForm(updatedFormClass.getId());
        if(form.isPresent()) {
            if(!backend.getFormSupervisor().getFormPermissions(formId).isSchemaUpdateAllowed()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            form.get().updateFormClass(updatedFormClass);

        } else {
            backend.createNewForm(updatedFormClass);
        }
        
        return Response.ok().build();
    }
    
    @GET
    @NoCache
    @Path("record/{recordId}")
    @Produces(JSON_CONTENT_TYPE)
    public FormRecord getRecord(@PathParam("recordId") String recordId) {
        
        FormStorage form = assertVisible(formId);

        Optional<FormRecord> record = form.get(ResourceId.valueOf(recordId));
        if(!record.isPresent()) {
            throw new NotFoundException("Record " + recordId + " does not exist.");
        }

        PermissionsEnforcer enforcer = backend.newPermissionsEnforcer();
        if(!enforcer.canView(record.get())) {
            throw new NotAuthorizedException();
        }

        return record.get();
    }

    @GET
    @NoCache
    @Path("records/versionRange")
    public FormSyncSet getVersionRange(
            @QueryParam("localVersion") long localVersion,
            @QueryParam("version") long version) {

        FormStorage collection = assertVisible(formId);

        // Compute a predicate that will tell us whether a given
        // record should be visible to the user, based on their *current* permissions.

        Predicate<ResourceId> visibilityPredicate = computeVisibilityPredicate();

        FormSyncSet syncSet;
        if(collection instanceof VersionedFormStorage) {
            syncSet = ((VersionedFormStorage) collection).getVersionRange(localVersion, version, visibilityPredicate);
        } else {
            syncSet = FormSyncSet.emptySet(formId);
        }
        return syncSet;
    }

    /**
     * Computes a record-level visibility predicate.
     */
    private Predicate<ResourceId> computeVisibilityPredicate() {
        FormPermissions formPermissions = backend.getFormSupervisor().getFormPermissions(formId);
        if (!formPermissions.hasVisibilityFilter()) {
            return Predicates.alwaysTrue();
        }

        QueryModel queryModel = new QueryModel(formId);
        queryModel.selectResourceId().as("id");

        ColumnSet columnSet = executeQuery(queryModel);
        ColumnView id = columnSet.getColumnView("id");
        final Set<String> idSet = new HashSet<>();
        for (int i = 0; i < id.numRows(); i++) {
            idSet.add(id.getString(i));
        }
        return new Predicate<ResourceId>() {
            @Override
            public boolean apply(ResourceId resourceId) {
                return idSet.contains(resourceId.asString());
            }
        };
    }

    @POST
    @Path("record/{recordId}/field/{fieldId}/geometry")
    public Response updateGeometry(
            @PathParam("recordId") String recordId,
            @PathParam("fieldId") String fieldId,
            byte[] binaryBody) {

        // Parse the Geometry
        WKBReader reader = new WKBReader(new GeometryFactory());
        Geometry geometry;
        try {
            geometry = reader.read(binaryBody);
        } catch (ParseException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Could not parse WKB geometry: " + e.getMessage())
                    .build();
        }

        geometry.normalize();

        if(!geometry.isValid()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(geometry.getGeometryType() + " is not valid")
                    .build();
        }

        // Check first to see if this form exists
        Optional<FormStorage> storage = backend.getStorage().getForm(formId);
        if(!storage.isPresent()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Form " + formId + " does not exist.")
                    .build();
        }

        // Find the field and verify that it's a GeoArea type
        FormField field;
        try {
            field = storage.get().getFormClass().getField(ResourceId.valueOf(fieldId));
        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Record " + recordId + " does not exist.")
                    .build();
        }
        if(!(field.getType() instanceof GeoAreaType)) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Field " + fieldId + " is not a GeoArea type")
                    .build();
        }

        try {
            storage.get().updateGeometry(ResourceId.valueOf(recordId), ResourceId.valueOf(fieldId), geometry);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update geometry for record " + recordId, e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }

        return Response.ok().build();
    }

    @GET
    @NoCache
    @Path("record/{recordId}/history")
    @Produces(JSON_CONTENT_TYPE)
    public RecordHistory getRecordHistory(@PathParam("recordId") String recordId) throws SQLException {

        assertVisible(formId);

        return backend.getRecordHistoryProvider().build(new RecordRef(formId, ResourceId.valueOf(recordId)));
    }

    @GET
    @NoCache
    @Path("records")
    @Produces(JSON_CONTENT_TYPE)
    public List<FormRecord> getRecords(@QueryParam("parentId") String parentId) {
        return assertVisible(formId).getSubRecords(ResourceId.valueOf(parentId));
    }

    @POST
    @Path("records")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRecord(JsonValue jsonObject) {
        
        try {
            backend.newUpdater().create(formId, jsonObject);
        } catch (InvalidUpdateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("record/{recordId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRecord(@PathParam("recordId") String recordId, JsonValue jsonObject) {

        try {
            backend.newUpdater().execute(formId, valueOf(recordId), jsonObject);
        } catch (InvalidUpdateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }
    
    @GET
    @NoCache
    @Produces(JSON_CONTENT_TYPE)
    @Path("class")
    public FormClass getFormClass() {
        return getFormSchema();
    }

    @GET
    @NoCache
    @Path("form.xls")
    public Response getXlsForm() {
        assertVisible(formId);

        final XlsFormBuilder xlsForm = new XlsFormBuilder(backend.getStorage());
        xlsForm.build(formId);

        StreamingOutput output = new StreamingOutput() {

            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                xlsForm.write(outputStream);
            }
        };

        return Response.ok(output, "application/vnd.ms-excel").build();
    }
    
    /**
     *
     * @return a list of {@link org.activityinfo.model.form.FormClass}es that includes the {@code FormClass}
     * of this collection and any {@code FormClass}es reachable from this collection's fields.
     */
    @GET
    @NoCache
    @Produces(JSON_CONTENT_TYPE)
    @Path("tree")
    public JsonValue getTree() {
        return JsonFormTreeBuilder.toJson(fetchTree());
    }

    @GET
    @NoCache
    @Produces(MediaType.TEXT_PLAIN)
    @Path("tree/pretty")
    public String getTreePrettyPrinted() {

        FormTree tree = fetchTree();
        StringWriter stringWriter = new StringWriter();
        FormTreePrettyPrinter printer = new FormTreePrettyPrinter(new PrintWriter(stringWriter));
        printer.printTree(tree);

        return stringWriter.toString();
    }

    private FormTree fetchTree() {
        assertVisible(formId);

        FormTreeBuilder builder = new FormTreeBuilder(backend.getStorage());
        return builder.queryTree(formId);
    }

    @GET
    @NoCache
    @Path("query/rows")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryRows(@Context UriInfo uriInfo) {
        final ColumnSet columnSet = query(uriInfo);

        LOGGER.info("Query completed with " + columnSet.getNumRows() + " rows.");

        final StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                RowBasedJsonWriter writer = new RowBasedJsonWriter(outputStream, Charsets.UTF_8);
                writer.write(columnSet);
                writer.flush();
            }
        };

        return Response
            .ok(output)
            .type(JSON_CONTENT_TYPE)
            .build();
    }

    @GET
    @NoCache
    @Path("query/defaultModel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDefaultQueryModel() {
        assertVisible(formId);

        QueryModel queryModel = buildDefaultQueryModel();

        return Response.ok(queryModel.toJsonString())
            .type(JSON_CONTENT_TYPE)
            .build();
    }


    @GET
    @NoCache
    @Path("query/columns")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryColumns(@Context UriInfo uriInfo) {
        final ColumnSet columnSet = query(uriInfo);

        final StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                ColumnJsonWriter columnSetWriter = new ColumnJsonWriter(outputStream, Charsets.UTF_8);
                columnSetWriter.write(columnSet);
                columnSetWriter.flush();
            }
        };

        return Response.ok(output).type(JSON_CONTENT_TYPE).build();
    }

    private ColumnSet query(final UriInfo uriInfo) {

        assertVisible(formId);

        QueryModel queryModel;

        if(uriInfo.getQueryParameters().isEmpty()) {
            queryModel = buildDefaultQueryModel();

        } else {
            queryModel = new QueryModel(formId);
            for (String columnId : uriInfo.getQueryParameters().keySet()) {
                queryModel.selectExpr(uriInfo.getQueryParameters().getFirst(columnId)).as(columnId);
            }
        }

        LOGGER.info(queryModel.toString());

        return executeQuery(queryModel);
    }

    private ColumnSet executeQuery(QueryModel queryModel) {
        return backend.newQueryBuilder().build(queryModel);
    }

    private QueryModel buildDefaultQueryModel() {
        QueryModel queryModel;
        FormTreeBuilder treeBuilder = new FormTreeBuilder(backend.getStorage());
        FormTree tree = treeBuilder.queryTree(formId);

        queryModel = new DefaultQueryBuilder(tree).build();
        return queryModel;
    }


    private FormStorage assertVisible(ResourceId formId) {
        Optional<FormStorage> storage = this.backend.getStorage().getForm(this.formId);
        if(!storage.isPresent()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity(format("Form %s does not exist.", formId.asString()))
                            .build());
        }
        FormPermissions permissions = backend.getFormSupervisor().getFormPermissions(formId);
        if(!permissions.isVisible()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity(format("You do not have permission to view the form %s", formId.asString()))
                            .build());

        }
        return storage.get();
    }
}