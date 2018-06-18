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
package org.activityinfo.store.query.shared;

import org.activityinfo.model.analysis.Analysis;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.form.RecordHistory;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;

/**
 * Source of form data and metadata for analysis components
 */
public interface FormSource {

    Observable<FormTree> getFormTree(ResourceId formId);

    Observable<Maybe<UserDatabaseMeta>> getDatabase(ResourceId databaseId);

    Observable<Maybe<RecordTree>> getRecordTree(RecordRef recordRef);

    Observable<Maybe<FormRecord>> getRecord(RecordRef recordRef);

    Observable<ColumnSet> query(QueryModel queryModel);

    Observable<Maybe<Analysis>> getAnalysis(String id);

    Observable<RecordHistory> getFormRecordHistory(RecordRef ref);
}
