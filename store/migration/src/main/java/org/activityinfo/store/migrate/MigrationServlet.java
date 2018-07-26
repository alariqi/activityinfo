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
package org.activityinfo.store.migrate;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.GoogleCloudStorageFileSet;
import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapSettings;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.outputs.GoogleCloudStorageFileOutput;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.PipelineServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import org.activityinfo.store.hrd.Hrd;
import org.activityinfo.store.hrd.entity.FormEntity;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

/**
 * Starts Map/Reduce jobs
 */
public class MigrationServlet extends HttpServlet {

    private final PipelineService pipelineService = PipelineServiceFactory.newPipelineService();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {



        String pipelineId;
        try {
            pipelineId = startJob(resp, req);
        } catch (IllegalArgumentException e) {
            resp.setStatus(400);
            resp.getOutputStream().println(e.getMessage());
            return;
        }
        if(pipelineId != null) {
            resp.sendRedirect("/_ah/pipeline/status.html?root=" + pipelineId);
        } else {
            resp.getWriter().println("Enqueued");
        }
    }

    private String startJob(HttpServletResponse resp, HttpServletRequest req) {
        String job = req.getParameter("job");
        boolean fix = "true".equals(req.getParameter("fix"));
        if(job == null) {
            throw new IllegalArgumentException("missing job parameter");
        }
        switch (job) {
            case "partners":
                return startPartnerMigration();
            case "snapshots":
                return reindexSnapshots();
            case "sites":
                return migrateSites(Integer.parseInt(req.getParameter("activityId")), fix);
            case "deleted-sites":
                return fixDeletedSites(fix);
            case "usage":
                return usageExport();
            case "blocks":
                return buildBlocks(req.getParameter("formId"));
            case "listsubforms":
                return listsubforms(resp);
            default:
                throw new IllegalArgumentException("Unknown job: " + job);
        }
    }

    private String listsubforms(HttpServletResponse resp) {
        ObjectifyService.run(new VoidWork() {
            @Override
            public void vrun() {
                try {
                    PrintWriter writer = resp.getWriter();
                    for (FormEntity formEntity : Hrd.ofy().load().type(FormEntity.class)) {
                        if(formEntity.getId().startsWith("c")) {
                            writer.println(formEntity.getId() + "," + formEntity.isColumnStorageActive());
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return null;
    }


    private String migrateSites(int activityId, boolean fix) {
        SiteInput input = new SiteInput(activityId);
        SiteMigrator mapper = new SiteMigrator(fix);

        MapSpecification<Integer, Void, Void> spec = new MapSpecification.Builder<Integer, Void, Void>(input, mapper)
                .setJobName("Migrate site records")
                .build();

        return MapJob.start(spec, getSettings());
    }

    private String fixDeletedSites(boolean fix) {
        DatastoreInput input = new DatastoreInput("FormRecord", 16);
        DeletedSiteFixer fixer = new DeletedSiteFixer(fix);

        MapSpecification<Entity, Void, Void> spec = new MapSpecification.Builder<Entity, Void, Void>(input, fixer)
                .setJobName("Fix deleted site records")
                .build();

        return MapJob.start(spec, getSettings());
    }

    private String reindexSnapshots() {
        DatastoreInput input = new DatastoreInput("FormRecordSnapshot", 10);
        SnapshotReindexer reindexer = new SnapshotReindexer();

        MapSpecification<Entity, Void, Void> spec = new MapSpecification.Builder<Entity, Void, Void>(input, reindexer)
                .setJobName("Reindex snapshot indexes")
                .build();

        return MapJob.start(spec, getSettings());
    }

    private String startPartnerMigration() {
        DatabaseInput input = new DatabaseInput();
        ProjectMigrator mapper = new ProjectMigrator();

        MapSpecification<Integer, Void, Void> spec = new MapSpecification.Builder<Integer, Void, Void>(input, mapper)
                .setJobName("Migrate MapReduce entities")
                .build();

        return MapJob.start(spec, getSettings());

    }

    private String usageExport() {
        DatastoreInput input = new DatastoreInput("FormRecordSnapshot",10);

        SnapshotExporter mapper = new SnapshotExporter();
        GoogleCloudStorageFileOutput output = new GoogleCloudStorageFileOutput(
                "activityinfoeu-bq-import",
                "update-snapshots-%d.csv", "text/csv");

        MapSpecification<Entity, ByteBuffer, GoogleCloudStorageFileSet> spec = new MapSpecification.Builder<>(input, mapper, output)
                .setJobName("Export snapshot events")
                .build();

        return MapJob.start(spec, getSettings());
    }


    private String buildBlocks(String formId) {
        return PipelineServiceFactory.newPipelineService().startNewPipeline(new BlockJob(formId));
    }


    public static MapSettings getSettings() {
        MapSettings settings = new MapSettings.Builder()
                .setWorkerQueueName("default")
                .setModule("migration")
                .build();
        return settings;
    }
}
