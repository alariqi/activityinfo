package org.activityinfo.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.base.Stopwatch;
import com.google.common.io.ByteSource;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.activityinfo.client.xform.XFormInstanceBuilder;
import org.activityinfo.client.xform.XFormItem;
import org.activityinfo.client.xform.XFormList;
import org.activityinfo.model.resource.*;
import org.activityinfo.service.blob.BlobId;
import org.activityinfo.service.blob.UploadCredentials;
import org.activityinfo.service.tasks.TaskModel;
import org.activityinfo.service.tasks.UserTask;
import org.w3c.dom.Document;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ActivityInfoClient {
    private final Client client;
    private final URI rootUri;
    protected final WebResource root;
    protected final WebResource store;

    public ActivityInfoClient(URI rootUri, String accountEmail, String password) {
        ClientConfig clientConfig = new DefaultClientConfig(
                JacksonJsonProvider.class,
                ObjectMapperProvider.class);

        this.rootUri = rootUri;

        client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter(accountEmail, password));
        client.addFilter(new LoggingFilter());

        root = client.resource(this.rootUri);
        store = root.path("service").path("store");
    }


    /**
     * Submits a completed XForm instance
     */
    public void submitXForm(XFormInstanceBuilder instance) {
        WebResource submission = client.resource(rootUri).path("submission");
        submission
            .type(MediaType.MULTIPART_FORM_DATA_TYPE)
            .entity(instance.build())
            .post();
    }

    /**
     *
     * @return a list of XForms which the user is authorized to submit.
     */
    public List<XFormItem> getXForms() {
        return client.resource(rootUri).path("formList")
            .type(MediaType.APPLICATION_XML_TYPE)
            .get(XFormList.class)
            .getForms();
    }

    /**
     * Retrieves an XForm as an XML document.
     */
    public Document getXForm(XFormItem formItem) {
        return client.resource(formItem.getUrl())
            .type(MediaType.APPLICATION_XML_TYPE)
            .get(Document.class);
    }

    /**
     * Submits an XForm Instance asynchronously.
     *
     * @param instance a completed XForm Instance
     */
    public Future<ClientResponse> submitXFormAsync(XFormInstanceBuilder instance) {
        FormDataMultiPart multipartBody = instance.build();

        AsyncWebResource.Builder resource = client.asyncResource(rootUri)
            .path("submission")
            .type(MediaType.MULTIPART_FORM_DATA_TYPE)
            .entity(multipartBody);

        return resource.post(ClientResponse.class);
    }

    /**
     * Retrieves the resource from the server
     * @param resourceId the resource's id.
     * @return
     */
    public Resource get(ResourceId resourceId) {
        return store.path("resource").path(resourceId.asString())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .get(UserResource.class)
                .getResource();
    }

    /**
     * Retrieves a resource's access control rules from the server.
     * @param resourceId the resource's id.
     * @return a list of resources representing the resource's access control rules
     */
    public List<Resource> getAccessControlRules(ResourceId resourceId) {
        return store.path("resource").path(resourceId.asString()).path("acr")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .get(new ResourceListGenericType());
    }

    /**
     * Creates a new resource
     */
    public void create(Resource resource) {
        store.path("resources")
            .type(MediaType.APPLICATION_JSON_TYPE)
            .post(resource);
    }


    /**
     * Starts the execution of background task on behalf of the user.
     *
     *
     * @param taskModel a description of the task to performed
     * @return a {@link org.activityinfo.service.tasks.UserTask} object with the task's
     * status and its id which can be used to query for its progress.
     */
    public UserTask startTask(TaskModel taskModel) {
        String taskId = Resources.generateId().asString();
        return root.path("service").path("tasks").path(taskId)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .post(UserTask.class, taskModel.asRecord());
    }

    /**
     * Synchronously waits for a task to complete or to fail.
     *
     * @param taskId the id of the task for which to wait
     * @param timeOut the amount of time to wait, or -1 for no limit
     * @param timeUnit the unit of the time limit
     * @return the completed {@code UserTask} object
     * @throws org.activityinfo.client.UserTaskException if the task fails or times out
     */
    public UserTask waitForTask(String taskId, long timeOut, TimeUnit timeUnit)  {
        Stopwatch stopwatch = Stopwatch.createStarted();
        UserTask updated;
        while(true) {
            updated = getTaskStatus(taskId);
            switch(updated.getStatus()) {
                case RUNNING:
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new UserTaskException(updated, "Timeout");
                    }
                    break;
                case FAILED:
                    throw new UserTaskException(updated, updated.getErrorMessage());
                case COMPLETE:
                    return updated;
            }
            if(timeOut > 0) {
                if(stopwatch.elapsed(timeUnit) > timeOut) {
                    throw new UserTaskException(updated, "Timeout");
                }
            }
        }
    }



    /**
     * Updates a resource
     */
    public void update(Resource resource) {
        store.path("resource")
            .path(resource.getId().asString())
            .type(MediaType.APPLICATION_JSON_TYPE)
            .put(resource);
    }

    /**
     * Deletes a resource
     */
    public void delete(ResourceId resourceId) {
        store.path("resource")
            .path(resourceId.asString())
            .type(MediaType.APPLICATION_JSON_TYPE)
            .delete();
    }

    /**
     *
     * @return a list of workspaces which the authenticated user owns
     * or to which they have been explicitly granted access.
     */
    public List<ResourceNode> getOwnedOrSharedWorkspaces() {
        return store.path("query")
            .path("roots")
            .type(MediaType.APPLICATION_JSON_TYPE)
            .get(new ResourceNodeListGenericType());
    }


    /**
     * Uploads a blob to the server.
     *
     * <p>Blobs are used to store large text and binary content apart from the resources
     * with which they are associated.</p>
     *
     * @param blobId
     * @param byteSource
     */
    public void postBlob(BlobId blobId, String fileName, MediaType mediaType, ByteSource byteSource) throws IOException {

        Form form = new Form();
        form.putSingle("blobId", blobId.asString());
        form.putSingle("filename", fileName);

        // First retrieve the upload URL
        UploadCredentials credentials = root.path("service")
            .path("blob")
            .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
            .accept(MediaType.APPLICATION_JSON)
            .post(UploadCredentials.class, form);

        System.out.println(credentials);

        // Create a multipart body that include the credentials
        // we just received
        FormDataMultiPart entity = new FormDataMultiPart();
        for (Map.Entry<String, String> entry : credentials.getFormFields().entrySet()) {
            entity.field(entry.getKey(), entry.getValue());
        }

        // Add the blob to upload
        InputStream inputStream = byteSource.openStream();
        entity.field("file", inputStream, mediaType);


        // Create a new client instance to submit to GCS without our
        // AI credentials. (authorization is included the params received above)
        Client.create()
            .resource(credentials.getUrl())
            .type(MediaType.MULTIPART_FORM_DATA_TYPE)
            .post(entity);
    }

    /**
     * Retrieves the blob with the given {@code blobId} from the server.
     *
     */
    public ByteSource getBlob(final BlobId blobId) {
        return new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                ClientResponse response = root.path("service")
                    .path("blob")
                    .path(blobId.asString())
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

                System.out.println(response);

                return response.getEntityInputStream();
            }
        };
    }

    /**
     *
     * @return a list of recent tasks running or completed on behalf of the user.
     */
    public List<UserTask> getTasks() {
        return root.path("service").path("tasks")
            .accept(MediaType.APPLICATION_JSON)
            .get(new TasksListGenericType());
    }


    /**
     * Retrieves the latest status of a background task running on behalf of the user
     * @param task the original task
     * @return a new, updated {@code UserTask} object
     */
    public UserTask getTaskStatus(UserTask task) {
        return getTaskStatus(task.getId());
    }


    /**
     * Retrieves the latest status of a background task running on behalf of the user
     * @param taskId the taskId
     * @return an updated {@code UserTask} object
     */
    public UserTask getTaskStatus(String taskId) {
        return root.path("service").path("tasks").path(taskId)
            .accept(MediaType.APPLICATION_JSON)
            .get(UserTask.class);
    }

    /**
     * Starts an import of a data file into an ActivityInfo Form and FormInstances.
     *
     * @param ownerId the workspace or folder where the new form will be created.
     * @param blobId the blobId of the data file to import.
     * @return a UserTask handle that can be used to track the status of the import job.
     */
    public UserTask startImport(ResourceId ownerId, BlobId blobId) {

        Form form = new Form();
        form.putSingle("ownerId", ownerId.asString());
        form.putSingle("blobId", blobId.asString());

        return root.path("service").path("load")
            .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
            .accept(MediaType.APPLICATION_JSON)
            .post(UserTask.class, form);
    }

    public List<Resource> getUpdates(ResourceId workspaceId, long version) {
        return store.path("query")
            .path("updates")
            .path(workspaceId.asString())
            .queryParam("version", String.valueOf(version))
            .accept(MediaType.APPLICATION_JSON)
            .get(new ResourceListGenericType());
    }

    final static private class ResourceNodeListGenericType extends GenericType<List<ResourceNode>> {
    }

    final static private class TasksListGenericType extends GenericType<List<UserTask>> {
    }

    final static private class ResourceListGenericType extends GenericType<List<Resource>> {
    }
}