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
package org.activityinfo.test.ui;

import org.activityinfo.test.driver.UiApplicationDriver;
import org.activityinfo.test.pageobject.gxt.GxtGrid;
import org.activityinfo.test.pageobject.web.ApplicationPage;
import org.junit.Test;

import javax.inject.Inject;

import static org.activityinfo.test.driver.Property.property;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.junit.Assert.assertThat;


public class UserSortingUiTest {
    
    @Inject
    public UiApplicationDriver driver;
    
    @Test
    public void test() throws Exception {

        String database = "My db";
        
        driver.login();
        driver.setup().createDatabase(property("name", database));
        
        String[] partners = new String[] { "ARC", "NRC", "RI" };
        for(String partner : partners) {
            driver.setup().addPartner(partner, database);
        }

        for(int i=0;i<300;++i) {
            String name = ('A' + (i % 26)) + " Jones";
            String user = String.format("user%d@example.com", i);
            driver.setup().grantPermission(
                    property("name", name),
                    property("partner", partners[i % partners.length]),
                    property("database", database),
                    property("user", user),
                    property("permissions", "View"));
        }

        ApplicationPage app = driver.getApplicationPage();
        GxtGrid userGrid = app.navigateToDesignTab().selectDatabase(driver.alias(database)).users().grid();
        userGrid.waitUntilAtLeastOneRowIsLoaded();
        
        // Verify that partners are sorted over the three several pages
        // and not just within the current page
        userGrid.sortBy("partner");
        assertThat(userGrid.columnValues("partner"), everyItem(equalTo(driver.alias("ARC"))));
    }
}
