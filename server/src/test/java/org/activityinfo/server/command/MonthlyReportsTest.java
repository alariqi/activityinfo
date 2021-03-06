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
package org.activityinfo.server.command;

import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.legacy.shared.command.GetMonthlyReports;
import org.activityinfo.legacy.shared.command.UpdateMonthlyReports;
import org.activityinfo.legacy.shared.command.result.MonthlyReportResult;
import org.activityinfo.legacy.shared.exception.CommandException;
import org.activityinfo.model.type.time.Month;
import org.activityinfo.server.database.OnDataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class MonthlyReportsTest extends CommandTestCase {

    @Before
    public void setUser() {
        setUser(1);
    }

    @Test
    public void testMonthCompare() throws Exception {

        Month feb = new Month(2009, 2);

        Month maxMonth = new Month(2009, 2);

        Assert.assertEquals(0, maxMonth.compareTo(feb));
    }

    @Test
    public void testGetReports() throws Exception {

        GetMonthlyReports cmd = new GetMonthlyReports(6);
        cmd.setStartMonth(new Month(2009, 1));
        cmd.setEndMonth(new Month(2009, 2));

        MonthlyReportResult result = execute(cmd);

        Assert.assertEquals(1, result.getData().size());
        Assert.assertEquals(35, result.getData().get(0).getValue(2009, 1).intValue());
        Assert.assertEquals(70, result.getData().get(0).getValue(2009, 2).intValue());
    }

    @Test
    public void testGetReportsWhenEmpty() throws Exception {

        GetMonthlyReports cmd = new GetMonthlyReports(7);
        cmd.setStartMonth(new Month(2009, 1));
        cmd.setEndMonth(new Month(2009, 2));

        MonthlyReportResult result = execute(cmd);

        Assert.assertEquals(1, result.getData().size());
    }

    @Test
    public void testUpdate() throws Exception {
        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 1), 45.0));
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 3), 22.0));

        execute(new UpdateMonthlyReports(6, changes));

        // verify that that changes have been made
        GetMonthlyReports cmd = new GetMonthlyReports(6);
        cmd.setStartMonth(new Month(2009, 1));
        cmd.setEndMonth(new Month(2009, 3));

        MonthlyReportResult result = execute(cmd);

        Assert.assertEquals(1, result.getData().size());
        Assert.assertEquals(45, result.getData().get(0).getValue(2009, 1).intValue());
        Assert.assertEquals(70, result.getData().get(0).getValue(2009, 2).intValue());
        Assert.assertEquals(22, result.getData().get(0).getValue(2009, 3).intValue());
    }

    @Test(expected = CommandException.class)
    public void unauthorized() {

        setUser(4); // marlene: viewall, but not editall

        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 1), 45.0));
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 3), 22.0));

        execute(new UpdateMonthlyReports(6, changes));
    }

    @Test
    public void authorizedByEditAll() {

        setUser(2); // bavon: viewall AND editall

        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 1), 45.0));
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 3), 22.0));

        execute(new UpdateMonthlyReports(6, changes));
    }

    @Test
    public void authorizedByEdit() {
        setUser(21); // pacifique, only edit

        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 1), 45.0));

        execute(new UpdateMonthlyReports(6, changes));
    }
}
