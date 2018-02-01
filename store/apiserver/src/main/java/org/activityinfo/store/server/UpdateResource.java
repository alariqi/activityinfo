package org.activityinfo.store.server;

import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.store.query.server.InvalidUpdateException;
import org.activityinfo.store.query.server.Updater;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Endpoint that handles transactional updates
 */
public class UpdateResource {

    private ApiBackend backend;

    public UpdateResource(ApiBackend backend) {
        this.backend = backend;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(String json) {

        final JsonValue jsonElement = Json.parse(json);

        Updater updater = backend.newUpdater();
        try {
            updater.execute(jsonElement);
        } catch (InvalidUpdateException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build());
        }
        return Response.ok().build();
    }
}
