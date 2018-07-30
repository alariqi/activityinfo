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
package org.activityinfo.ui.client.store.http;

import org.activityinfo.api.client.ActivityInfoClientAsync;
import org.activityinfo.model.job.JobDescriptor;
import org.activityinfo.model.job.JobStatus;
import org.activityinfo.promise.Promise;


public class StartJobRequest implements HttpRequest<JobStatus> {

    private final JobDescriptor job;

    public StartJobRequest(JobDescriptor job) {
        this.job = job;
    }

    @Override
    public Promise<JobStatus> execute(ActivityInfoClientAsync async) {
        return async.startJob(job);
    }


    @Override
    public int refreshInterval(JobStatus result) {
        return -1;
    }
}
