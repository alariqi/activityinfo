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
package org.activityinfo.ui.client.local.sync.pipeline;

import com.bedatadriven.rebar.async.AsyncCommand;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.fn.AsyncSql;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.ui.client.local.command.CommandQueue;
import org.activityinfo.ui.client.local.sync.SyncHistoryTable;
import org.activityinfo.ui.client.local.sync.SyncRegionTable;

public class DropAll implements AsyncCommand {

    private final SqlDatabase conn;

    @Inject
    public DropAll(SqlDatabase conn) {
        super();
        this.conn = conn;
    }

    @Override
    public void execute(final AsyncCallback<Void> callback) {

        conn.execute(AsyncSql.sequence(AsyncSql.dropAllTables(),
                new SyncRegionTable(conn).createTableIfNotExists(),
                new SyncHistoryTable(conn).createTableIfNotExists(),
                CommandQueue.createTableIfNotExists()), callback);
    }
}
