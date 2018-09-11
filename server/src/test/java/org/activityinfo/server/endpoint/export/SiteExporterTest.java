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
package org.activityinfo.server.endpoint.export;

import org.activityinfo.legacy.shared.command.Filter;
import org.activityinfo.legacy.shared.command.GetSites;
import org.activityinfo.legacy.shared.command.result.SiteResult;
import org.activityinfo.legacy.shared.model.*;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.report.NullStorageProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SiteExporterTest {

    @Test
    public void sheetNameTest() {

        CountryDTO somalia = new CountryDTO(1, "Somalia");
        LocationTypeDTO locationType = new LocationTypeDTO(1, "Village");
        locationType.setAdminLevels(somalia.getAdminLevels());

        somalia.getLocationTypes().add(locationType);

        UserDatabaseDTO syli = new UserDatabaseDTO();
        syli.setId(444);
        syli.setName("SYLI");
        syli.setCountry(somalia);

        ActivityFormDTO activity = new ActivityFormDTO();
        activity.setId(1);
        activity.setDatabase(syli);
        activity.setName("Construction/Rehabilitation of Sec. Schools");
        activity.setLocationType(locationType);

        ActivityFormDTO activity2 = new ActivityFormDTO();
        activity2.setId(2);
        activity2.setDatabase(syli);
        activity2.setName("Construction/Rehabilitation of Primary Schools");
        activity2.setLocationType(locationType);

        ActivityFormDTO activity3 = new ActivityFormDTO();
        activity3.setId(3);
        activity3.setDatabase(syli);
        activity3.setName("Construction Rehabil (2)");
        activity3.setLocationType(locationType);


        DispatcherSync dispatcher = createMock(DispatcherSync.class);
        expect(dispatcher.execute(isA(GetSites.class))).andReturn(new SiteResult(new ArrayList<SiteDTO>())).anyTimes();
        replay(dispatcher);

        Filter filter = new Filter();

        SiteExporter exporter = new SiteExporter(new TaskContext(dispatcher, new NullStorageProvider(), "XYZ"));
        exporter.export(activity, filter);
        exporter.export(activity2, filter);
        exporter.export(activity3, filter);
        HSSFWorkbook book = exporter.getBook();

        assertThat(book.getSheetAt(0).getSheetName(), equalTo("Construction Rehabilitation of "));
        assertThat(book.getSheetAt(1).getSheetName(), equalTo("Construction Rehabilitation"));
        assertThat(book.getSheetAt(2).getSheetName(), equalTo("Construction Rehabil 2"));
    }
}
