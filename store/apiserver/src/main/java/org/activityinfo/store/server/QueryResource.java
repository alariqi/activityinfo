package org.activityinfo.store.server;

import com.google.common.base.Charsets;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.store.query.output.ColumnJsonWriter;
import org.activityinfo.store.query.server.ColumnSetBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.zip.GZIPOutputStream;


public class QueryResource {

    private static final boolean APPENGINE = (System.getenv("com.google.appengine.runtime.environment") != null);

    private final ApiBackend backend;

    public QueryResource(ApiBackend backend) {
        this.backend = backend;
    }

    @POST
    @Path("columns")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryColumns(@HeaderParam("Accept-Encoding") String acceptEncoding, QueryModel model) {

        ColumnSetBuilder builder = backend.newQueryBuilder();

        final ColumnSet columnSet = builder.build(model);

        // If the client supports it, write the JSON directly to
        // a Gzip output stream.

        // In the AppEngine environment, all content-encoding is handled
        // by the surrounding runtime, so this is not option here.

        if(!APPENGINE && clientSupportsGzip(acceptEncoding)) {
            return streamingCompressedResponse(columnSet);
        } else {
            return streamingResponse(columnSet);
        }
    }

    private boolean clientSupportsGzip(String acceptEncoding) {
        if(acceptEncoding == null) {
            return false;
        }
        return acceptEncoding.toLowerCase().contains("gzip");
    }

    private Response streamingResponse(ColumnSet columnSet) {

        StreamingOutput output = outputStream -> {
            ColumnJsonWriter columnSetWriter = new ColumnJsonWriter(outputStream, Charsets.UTF_8);
            columnSetWriter.write(columnSet);
            columnSetWriter.flush();
        };
        return Response.ok(output).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    private Response streamingCompressedResponse(ColumnSet columnSet) {
        StreamingOutput output = outputStream -> {
            GZIPOutputStream zippedOut = new GZIPOutputStream(outputStream);
            ColumnJsonWriter columnSetWriter = new ColumnJsonWriter(zippedOut, Charsets.UTF_8);
            columnSetWriter.write(columnSet);
            columnSetWriter.flush();
            zippedOut.finish();
        };

        return Response.ok(output)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Encoding", "gzip")
                .build();
    }

}
