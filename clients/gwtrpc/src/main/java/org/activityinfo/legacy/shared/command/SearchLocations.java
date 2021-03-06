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
package org.activityinfo.legacy.shared.command;

import org.activityinfo.legacy.shared.command.result.LocationResult;

import java.util.Collection;

public class SearchLocations implements Command<LocationResult> {

    private Collection<Integer> adminEntityIds;
    private Collection<Integer> indicatorIds;
    private String name;
    private int limit = -1;
    private int locationTypeId = 0;

    public Collection<Integer> getAdminEntityIds() {
        return adminEntityIds;
    }

    public SearchLocations setAdminEntityIds(Collection<Integer> adminEntityIds) {
        this.adminEntityIds = adminEntityIds;
        return this;
    }

    public String getName() {
        return name;
    }

    public SearchLocations setName(String name) {
        this.name = name;
        return this;
    }

    public int getLocationTypeId() {
        return locationTypeId;
    }

    public SearchLocations setLocationTypeId(int locationTypeId) {
        this.locationTypeId = locationTypeId;
        return this;
    }

    public Collection<Integer> getIndicatorIds() {
        return indicatorIds;
    }

    public SearchLocations setIndicatorIds(Collection<Integer> indicatorIds) {
        this.indicatorIds = indicatorIds;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public SearchLocations setLimit(int limit) {
        this.limit = limit;
        return this;
    }
}
