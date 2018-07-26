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
package org.activityinfo.server.digest.geo;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.inject.Inject;
import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.fixtures.Modules;
import org.activityinfo.fixtures.TestHibernateModule;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.Database;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.digest.TestDigestModule;
import org.activityinfo.server.digest.UserDigest;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/digests.db.xml")
@Modules({
        TestHibernateModule.class,
        TestDigestModule.class
})
public class GeoDigestModelBuilderTest {
    @Inject
    private GeoDigestModelBuilder geoDigestModelBuilder;
    @Inject
    private EntityManager em;

    @Test
    public void testFindDatabasesOwnerAndViewAndNotification() throws Exception {
        // owner & view & notification
        User user = em.find(User.class, 100);
        List<Database> dbs = geoDigestModelBuilder.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(2)));
        assertTrue(dbs.contains(em.find(Database.class, 3)));
        assertTrue(dbs.contains(em.find(Database.class, 100)));
    }

    @Test
    public void testFindDatabasesOwnerAndNotification() throws Exception {
        // owner & notification
        User user = em.find(User.class, 1);
        List<Database> dbs = geoDigestModelBuilder.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(2)));
        assertTrue(dbs.contains(em.find(Database.class, 1)));
        assertTrue(dbs.contains(em.find(Database.class, 2)));
    }

    @Test
    public void testFindDatabasesOwnerAndNoNotification() throws Exception {
        // owner & no notification
        User user = em.find(User.class, 2);
        List<Database> dbs = geoDigestModelBuilder.findDatabases(user);
        dbs = geoDigestModelBuilder.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(0)));
    }

    @Test
    public void testFindDatabasesViewAndNotification() throws Exception {
        // view & notification
        User user = em.find(User.class, 3);
        List<Database> dbs = geoDigestModelBuilder.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(1)));
        assertTrue(dbs.contains(em.find(Database.class, 1)));
    }

    @Test
    public void testFindDatabasesOnlyNotification() throws Exception {
        // only notification
        User user = em.find(User.class, 7);
        List<Database> dbs = geoDigestModelBuilder.findDatabases(user);
        assertThat(dbs.size(), is(equalTo(0)));
    }

    @Test
    public void testEmptyDigestsAreNotSent() throws Exception {
        // only notification
        User user = em.find(User.class, 1);
        LocalDate today = new LocalDate(2041, 1, 1);
        UserDigest userDigest = new UserDigest(user, today.atMidnightInMyTimezone(), 1);
        GeoDigestModel model = geoDigestModelBuilder.createModel(userDigest);
        assertThat(model.hasData(), equalTo(false));
    }

    @Test
    public void testFindSitesNoSiteDatabase() {
        List<Integer> sites = geoDigestModelBuilder.findSiteIds(
                em.find(Database.class, 2), 1300000000000L);
        assertThat(sites.size(), is(equalTo(0)));
    }

    @Test
    public void testFindSitesBeforeFrom() {
        List<Integer> sites = geoDigestModelBuilder.findSiteIds(
                em.find(Database.class, 1), 1370000000000L);
        assertThat(sites.size(), is(equalTo(0)));
    }

    @Test
    public void testFindSitesBetween() {
        List<Integer> sites = geoDigestModelBuilder.findSiteIds(
                em.find(Database.class, 1), 1360000000000L);
        assertThat(sites.size(), is(equalTo(1)));
        assertTrue(sites.contains(1));
    }

    @Test
    public void testFindSitesAfterFrom() {
        List<Integer> sites = geoDigestModelBuilder.findSiteIds(
                em.find(Database.class, 1), 1350000000000L);
        assertThat(sites.size(), is(equalTo(2)));
        assertTrue(sites.contains(1));
        assertTrue(sites.contains(2));
    }
}