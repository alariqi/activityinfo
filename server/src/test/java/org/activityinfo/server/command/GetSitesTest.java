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

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.SortInfo;
import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.command.Filter;
import org.activityinfo.legacy.shared.command.GetSites;
import org.activityinfo.legacy.shared.command.result.SiteResult;
import org.activityinfo.legacy.shared.exception.CommandException;
import org.activityinfo.legacy.shared.model.AdminEntityDTO;
import org.activityinfo.legacy.shared.model.IndicatorDTO;
import org.activityinfo.legacy.shared.model.SiteDTO;
import org.activityinfo.server.database.OnDataSet;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetSitesTest extends CommandTestCase2 {
    private static final int DATABASE_OWNER = 1;

    @Test
    public void testActivityQueryBasic() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo("date2", SortDir.DESC));

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("totalLength", 3, result.getData().size());
        Assert.assertEquals("totalLength", 3, result.getTotalLength());
        Assert.assertEquals("offset", 0, result.getOffset());
        // Assert.assertNull("row(0).activity",
        // result.getData().get(0).getActivity());

        // assure sorted
        Assert.assertEquals("sorted", 2, result.getData().get(0).getId());
        Assert.assertEquals("sorted", 1, result.getData().get(1).getId());
        Assert.assertEquals("sorted", 3, result.getData().get(2).getId());

        // assure indicators are present (site id=3)
        SiteDTO s = result.getData().get(2);

        Assert.assertEquals("entityName", "Ituri", s.getAdminEntity(1)
                .getName());
        try {
            Assert.assertNotNull("admin bounds", s.getAdminEntity(1).getBounds());
            throw new CommandException("Admin Bounds Location extraction enabled - remove AssertionError catch");
        } catch (AssertionError err) {
            // TODO: Admin Bounds Location extraction disabled until "ST_" functions corrected on QueryEngine
            // Remove try{} catch{} when enabled and I start throwing CommandExceptions
        }
        Assert.assertThat("indicator", s.getIndicatorValue(1), equalTo(10000.0));
        Assert.assertNull("site x", s.getX());

        // assure project is present
        SiteDTO s1 = result.getData().get(1);
        assertThat(s1.getId(), equalTo(1));
        assertThat(s1.getProject().getId(), equalTo(3));

    }

    @Test
    public void testIndicatorSort() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo(IndicatorDTO.getPropertyName(1), SortDir.DESC));

        PagingLoadResult<SiteDTO> result = execute(cmd);

        // assure sorted
        assertThat("sorted", result.getData().get(0).getIndicatorValue(1), equalTo(10000.0));
        assertThat("sorted", result.getData().get(1).getIndicatorValue(1), closeTo(3600.0, 1d));
        assertThat("sorted", result.getData().get(2).getIndicatorValue(1), closeTo(1500.0, 1d));

        Assert.assertNotNull("activityId", result.getData().get(0).getActivityId());
    }

    @Test
    @OnDataSet("/dbunit/sites-public.db.xml")
    public void testAdminEntitySort() throws CommandException {

        setUser(DATABASE_OWNER);

        // level 1 - DESC
        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo("E1", SortDir.DESC));

        PagingLoadResult<SiteDTO> result = execute(cmd);

        assertThat("sorted", adminName(result, 0, "E1"), equalTo("Sud Kivu"));
        assertThat("sorted", adminName(result, 1, "E1"), equalTo("Sud Kivu"));
        assertThat("sorted", adminName(result, 2, "E1"), equalTo("Ituri"));

        // level 1 - ASC
        cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo("E1", SortDir.ASC));

        result = execute(cmd);

        assertThat("sorted", adminName(result, 0, "E1"), equalTo("Ituri"));
        assertThat("sorted", adminName(result, 1, "E1"), equalTo("Sud Kivu"));
        assertThat("sorted", adminName(result, 2, "E1"), equalTo("Sud Kivu"));

        // level 2 - DESC
        cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo("E2", SortDir.DESC));

        result = execute(cmd);

        assertThat("sorted", adminName(result, 0, "E2"), equalTo("Walungu"));
        assertThat("sorted", adminName(result, 1, "E2"), equalTo("Shabunda"));
        assertThat("sorted", adminName(result, 2, "E2"), equalTo("Irumu"));

        // level 2 - ASC
        cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo("E2", SortDir.ASC));

        result = execute(cmd);

        assertThat("sorted", adminName(result, 0, "E2"), equalTo("Irumu"));
        assertThat("sorted", adminName(result, 1, "E2"), equalTo("Shabunda"));
        assertThat("sorted", adminName(result, 2, "E2"), equalTo("Walungu"));
    }

    private String adminName(PagingLoadResult<SiteDTO> result, int index, String key) {
        SiteDTO site = result.getData().get(index);
        return ((AdminEntityDTO) site.get(key)).getName();
    }


    @Ignore
    @Test
    public void testActivityQueryPaged() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo(IndicatorDTO.getPropertyName(1),
                SortDir.DESC));
        cmd.setLimit(2);
        cmd.setOffset(0);

        PagingLoadResult<SiteDTO> result = execute(cmd);

        assertThat("offset", result.getOffset(), equalTo(0));

        cmd.setOffset(1);
        cmd.setLimit(2);

        result = execute(cmd);

        assertThat(result.getOffset(), equalTo(1));
        assertThat(result.getData().size(), equalTo(2));
        assertThat("total length", result.getTotalLength(), equalTo(3));

        cmd.setOffset(0);
        cmd.setLimit(50);

        result = execute(cmd);

        assertThat(result.getOffset(), equalTo(0));
        assertThat(result.getData().size(), equalTo(3));
        assertThat("total length", result.getTotalLength(), equalTo(3));

    }

    @Test
    public void testDatabase() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites legacyCmd = new GetSites();
        GetSites newCmd = new GetSites();

        legacyCmd.filter().addRestriction(DimensionType.Database, 2);
        legacyCmd.setLegacyFetch(true);

        newCmd.filter().addRestriction(DimensionType.Database, 2);
        newCmd.setLegacyFetch(false);

        PagingLoadResult<SiteDTO> legacyResult = execute(legacyCmd);
        PagingLoadResult<SiteDTO> newResult = execute(newCmd);

        Assert.assertEquals("rows", 3, legacyResult.getData().size());
        Assert.assertNotNull("activityId", legacyResult.getData().get(0)
                .getActivityId());

        Assert.assertEquals("rows", 3, newResult.getData().size());
        Assert.assertNotNull("activityId", newResult.getData().get(0)
                .getActivityId());

        GetSites newMonthlyCmd = new GetSites();
        newMonthlyCmd.filter().addRestriction(DimensionType.Database, 2);
        newMonthlyCmd.setLegacyFetch(false);
        newMonthlyCmd.setFetchAllReportingPeriods(true);

        PagingLoadResult<SiteDTO> newMonthlyResult = execute(newMonthlyCmd);

        Assert.assertEquals("rows", 5, newMonthlyResult.getData().size());
    }

    @Test
    public void testDatabasePaged() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.getFilter().addRestriction(DimensionType.Database, 1);
        cmd.setLimit(2);

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("rows", 2, result.getData().size());

    }

    @Test
    public void testDatabasePartner2PartnerVisibility() throws CommandException {

        setUser(2); // BAVON (can't see other partner's stuff)

        GetSites cmd = new GetSites();
        cmd.getFilter().addRestriction(DimensionType.Database, 1);

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("rows", 3, result.getData().size());
    }

    @Test
    public void testAll() throws CommandException {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("rows", 8, result.getData().size());
        Assert.assertNotNull("activityId", result.getData().get(0)
                .getActivityId());
    }

    @Test
    public void testAllWithRemovedUser() throws CommandException {

        setUser(5); // Christian (Bad guy!)

        PagingLoadResult<SiteDTO> result = execute(new GetSites());

        Assert.assertEquals("rows", 0, result.getData().size());
    }

    @Test
    public void filterByIndicator() throws CommandException {
        setUser(1);

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Indicator, 5);

        SiteResult result = execute(new GetSites(filter));

        assertThat(result.getData().size(), equalTo(1));
        assertThat(result.getData().get(0).getId(), equalTo(9));
    }

    @Test
    public void filterByAttribute() throws CommandException {
        setUser(1);

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Activity, 1);
        filter.addRestriction(DimensionType.Attribute, 2);

        SiteResult result = execute(new GetSites(filter));

        assertThat(result.getData(), hasSize(1));
        assertThat(result.getData().get(0).getAttributeValue(2), equalTo(true));
    }


    @Test
    @Ignore
    public void testSeekSite() {

        setUser(DATABASE_OWNER);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo(IndicatorDTO.getPropertyName(1),
                SortDir.DESC));
        cmd.setLimit(2);
        cmd.setSeekToSiteId(1);

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("second page returned", 2, result.getOffset());
        Assert.assertEquals("rows on this page", 1, result.getData().size());
        Assert.assertEquals("correct site returned", 1, result.getData().get(0)
                .getId());
    }

    @Test
    @OnDataSet("/dbunit/sites-public.db.xml")
    public void testGetPublicSites() throws CommandException {
        int anonnoymsUserID = 0;
        setUser(anonnoymsUserID);

        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);
        cmd.setSortInfo(new SortInfo("date2", SortDir.DESC));

        PagingLoadResult<SiteDTO> result = execute(cmd);

        Assert.assertEquals("totalLength", 3, result.getData().size());
        Assert.assertEquals("totalLength", 3, result.getTotalLength());
        Assert.assertEquals("offset", 0, result.getOffset());
        // Assert.assertNull("row(0).activity",
        // result.getData().get(0).getActivity());

        // assure sorted
        Assert.assertEquals("sorted", 2, result.getData().get(0).getId());
        Assert.assertEquals("sorted", 1, result.getData().get(1).getId());
        Assert.assertEquals("sorted", 3, result.getData().get(2).getId());

        // assure indicators are present (site id=3)
        SiteDTO s = result.getData().get(2);

        Assert.assertEquals("entityName", "Ituri", s.getAdminEntity(1)
                .getName());
        Assert.assertNotNull("admin bounds", s.getAdminEntity(1).getBounds());
        Assert.assertThat("indicator", s.getIndicatorValue(1), equalTo(10000.0));
        Assert.assertNull("site x", s.getX());

        // assure project is present
        SiteDTO s1 = result.getData().get(1);
        assertThat(s1.getId(), equalTo(1));
        assertThat(s1.getProject().getId(), equalTo(1));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked3.db.xml")
    public void testCalculatedIndicatorQuery() throws CommandException {
        setUser(1);
        SiteDTO site;
        GetSites cmd = new GetSites();
        cmd.filter().onActivity(1);

        // Legacy Fetch
        cmd.setLegacyFetch(true);
        PagingLoadResult<SiteDTO> legacyResult = execute(cmd);
        List<SiteDTO> legacySites = legacyResult.getData();

        assertThat(legacyResult.getTotalLength(), equalTo(5));
        assertThat(legacySites.size(), equalTo(5));

        // first site should be entered site 2
        site = legacySites.get(0);
        assertThat(site.getId(), equalTo(2));
        assertFalse(site.isLinked());
        assertThat(site.getIndicatorValue(1), equalTo(400.0));
        assertNull(site.getIndicatorValue(2));
        assertThat(site.getIndicatorValue(5), equalTo(400.0));

        // second site should be entered site 5
        site = legacySites.get(1);
        assertThat(site.getId(), equalTo(5));
        assertFalse(site.isLinked());
        assertNull(site.getIndicatorValue(1));
        assertThat(site.getIndicatorValue(2), equalTo(400.0));
        assertThat(site.getIndicatorValue(5), equalTo(400.0));

        // third site should be linked site 1
        site = legacySites.get(2);
        assertThat(site.getId(), equalTo(1));
        assertTrue(site.isLinked());
        assertThat(site.getIndicatorValue(1), equalTo(1500.0));
        assertNull(site.getIndicatorValue(2));
        assertThat(site.getIndicatorValue(5), equalTo(1500.0));

        // fourth site should be linked site 3
        site = legacySites.get(3);
        assertThat(site.getId(), equalTo(3));
        assertTrue(site.isLinked());
        assertThat(site.getIndicatorValue(1), equalTo(1500.0));
        assertNull(site.getIndicatorValue(2));
        assertThat(site.getIndicatorValue(5), equalTo(1500.0));

        // fifth site should be linked site 4
        site = legacySites.get(4);
        assertThat(site.getId(), equalTo(4));
        assertTrue(site.isLinked());
        assertNull(site.getIndicatorValue(1));
        assertNull(site.getIndicatorValue(2));
        assertNull(site.getIndicatorValue(5));

        // New Fetch
        cmd.setLegacyFetch(false);
        PagingLoadResult<SiteDTO> newResult = execute(cmd);
        List<SiteDTO> newSites = newResult.getData();

        assertThat(newResult.getTotalLength(), equalTo(5));
        assertThat(newSites.size(), equalTo(5));

        // first site should be entered site 2
        site = newSites.get(0);
        assertThat(site.getId(), equalTo(2));
        assertFalse(site.isLinked());
        assertThat(site.getIndicatorValue(1), equalTo(400.0));
        assertNull(site.getIndicatorValue(2));
        assertThat(site.getIndicatorValue(5), equalTo(400.0));

        // second site should be entered site 5
        site = newSites.get(1);
        assertThat(site.getId(), equalTo(5));
        assertFalse(site.isLinked());
        assertNull(site.getIndicatorValue(1));
        assertThat(site.getIndicatorValue(2), equalTo(400.0));
        assertThat(site.getIndicatorValue(5), equalTo(400.0));

        // third site should be linked site 1
        site = newSites.get(2);
        assertThat(site.getId(), equalTo(1));
        assertTrue(site.isLinked());
        assertThat(site.getIndicatorValue(1), equalTo(1500.0));
        assertNull(site.getIndicatorValue(2));
        assertThat(site.getIndicatorValue(5), equalTo(1500.0));

        // fourth site should be linked site 3
        site = newSites.get(3);
        assertThat(site.getId(), equalTo(3));
        assertTrue(site.isLinked());
        assertThat(site.getIndicatorValue(1), equalTo(1500.0));
        assertNull(site.getIndicatorValue(2));
        assertThat(site.getIndicatorValue(5), equalTo(1500.0));

        // fifth site should be linked site 4
        site = newSites.get(4);
        assertThat(site.getId(), equalTo(4));
        assertTrue(site.isLinked());
        assertNull(site.getIndicatorValue(1));
        assertNull(site.getIndicatorValue(2));
        assertNull(site.getIndicatorValue(5));
    }

    @Test
    public void filterOnPartner() {
        setUser(1);

        GetSites cmd = new GetSites();
        cmd.filter().addRestriction(DimensionType.Project, 2);

        SiteResult result = execute(cmd);

        assertThat(result.getData().size(), equalTo(1));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void linkedSites() {
        setUser(1);

        GetSites cmd = new GetSites();
        cmd.filter().addRestriction(DimensionType.Activity, 1);
        cmd.setSortInfo(new SortInfo("locationName", SortDir.ASC));

        SiteResult result = execute(cmd);
        assertThat(result.getData().size(), equalTo(2));

        SiteDTO site1 = result.getData().get(0);
        SiteDTO site2 = result.getData().get(1);

        System.out.println(site1.getProperties());
        System.out.println(site2.getProperties());

        assertThat(site1.getId(), equalTo(1));
        assertThat(site1.getLocationName(), equalTo("Penekusu Kivu"));
        assertThat(site1.getActivityId(), equalTo(1));
        assertThat(site1.getIndicatorValue(1), equalTo(1500d));

        assertThat(site2.getId(), equalTo(2));
        assertThat(site2.getLocationName(), equalTo("Penekusu Kivu 2"));
        assertThat(site2.getActivityId(), equalTo(1));
        assertThat(site2.getIndicatorValue(1), equalTo(400d));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void linkedSitesFilteredByIndicator() {
        setUser(1);

        GetSites cmd = new GetSites();
        cmd.filter().addRestriction(DimensionType.Indicator, 1);
        cmd.setSortInfo(new SortInfo("locationName", SortDir.ASC));

        SiteResult result = execute(cmd);
        assertThat(result.getData().size(), equalTo(2));

        SiteDTO site1 = result.getData().get(0);
        SiteDTO site2 = result.getData().get(1);

        System.out.println(site1.getProperties());
        System.out.println(site2.getProperties());

        assertThat(site1.getId(), equalTo(1));
        assertThat(site1.getLocationName(), equalTo("Penekusu Kivu"));
        assertThat(site1.getActivityId(), equalTo(1));
        assertThat(site1.getIndicatorValue(1), equalTo(1500d));

        assertThat(site2.getId(), equalTo(2));
        assertThat(site2.getLocationName(), equalTo("Penekusu Kivu 2"));
        assertThat(site2.getActivityId(), equalTo(1));
        assertThat(site2.getIndicatorValue(1), equalTo(400d));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void duplicated() {

        // A given site can be appear multiple times in the list if it is the
        // source
        // of one or more indicators

        setUser(1);

        GetSites cmd = new GetSites();

        SiteResult result = execute(cmd);
        assertThat(result.getData().size(), equalTo(3));

    }
}
