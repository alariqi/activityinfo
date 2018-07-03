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
package org.activityinfo.server.job;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.inject.util.Providers;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.util.Closeable;
import org.activityinfo.json.JsonValue;
import org.activityinfo.legacy.shared.AuthenticatedUser;
import org.activityinfo.model.analysis.ImmutableTableAnalysisModel;
import org.activityinfo.model.analysis.TableAnalysisModel;
import org.activityinfo.model.job.ExportFormJob;
import org.activityinfo.model.job.JobRequest;
import org.activityinfo.model.job.JobState;
import org.activityinfo.model.job.JobStatus;
import org.activityinfo.model.resource.ResourceId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class JobResourceTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100),
                    new LocalTaskQueueTestConfig());

    private int userId = 1;
    private Closeable objectifyCloseable;

    @Before
    public void setUp() {
        helper.setUp();
        objectifyCloseable = ObjectifyService.begin();
    }

    @After
    public void tearDown() {
        helper.tearDown();
        objectifyCloseable.close();
    }

    @Test
    public void startJob() {
        final org.activityinfo.json.JsonParser parser = new org.activityinfo.json.JsonParser();
        final Queue queue = QueueFactory.getDefaultQueue();
        final AuthenticatedUser user = new AuthenticatedUser("XYZ", 1, "akbertram@gmail.com");
        final JobResource resource = new JobResource(Providers.of(user), queue);


        // First request starts the job

        final String jobId = ObjectifyService.run(new Work<String>() {
            @Override
            public String run() {

                TableAnalysisModel tableModel = ImmutableTableAnalysisModel.builder().formId(ResourceId.valueOf("FORM1")).build();

                ExportFormJob exportForm = new ExportFormJob(tableModel);
                JobRequest request = new JobRequest(exportForm, "en");


                Response response = resource.start(request.toJsonObject().toJson());

                JsonValue resultObject = parser.parse((String) response.getEntity());

                JobStatus result = JobStatus.fromJson(resultObject);

                assertThat(result.getState(), equalTo(JobState.STARTED));
                return result.getId();
            }
        });

        // Second request retrieves status
        ObjectifyService.run(new VoidWork() {
            @Override
            public void vrun() {

                Response statusResponse = resource.get(jobId);
                JsonValue statusObject = parser.parse(((String) statusResponse.getEntity()));
                JobStatus status = JobStatus.fromJson(statusObject);

                assertThat(status.getState(), equalTo(JobState.STARTED));
            }
        });

    }
}