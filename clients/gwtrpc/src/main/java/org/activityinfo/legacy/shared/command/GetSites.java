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

import com.google.common.collect.Sets;
import org.activityinfo.legacy.shared.command.result.SiteResult;

import java.util.Collection;
import java.util.Set;

/**
 * Retrieves a list of sites based on the provided filter and limits.
 *
 * @author Alex Bertram
 */
public class GetSites extends PagingGetCommand<SiteResult> implements Command<SiteResult> {

    private Filter filter = new Filter();

    private Integer seekToSiteId;
    private boolean fetchAttributes = true;
    private boolean fetchAllIndicators = true;
    private Set<Integer> fetchIndicators;
    private boolean fetchAdminEntities = true;

    private boolean fetchPartner = true;
    private boolean fetchLocation = true;
    private boolean fetchLinks = true;
    private boolean fetchComments = true;
    private boolean fetchDates = true;
    private boolean fetchAllReportingPeriods = false;

    private boolean legacyFetch = false;

    public GetSites() {
    }

    public GetSites(Filter filter) {
        this.filter = filter;
    }

    public boolean isLegacyFetch() {
        return legacyFetch;
    }

    public void setLegacyFetch(boolean legacyFetch) {
        this.legacyFetch = legacyFetch;
    }

    public boolean hasSingleActivity() {
        return filter.getRestrictions(DimensionType.Activity).size() == 1;
    }

    public Filter filter() {
        return filter;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        assert filter != null : "Filter cannot be null! Use new Filter() to create an empty filter";
        this.filter = filter;
    }

    public static GetSites byId(int siteId) {
        GetSites cmd = new GetSites();
        cmd.filter().addRestriction(DimensionType.Site, siteId);

        return cmd;
    }

    public boolean isFetchLinks() {
        return fetchLinks;
    }

    public void setFetchLinks(boolean fetchLinks) {
        this.fetchLinks = fetchLinks;
    }

    public static GetSites byActivity(int activityId) {
        GetSites cmd = new GetSites();
        cmd.filter().onActivity(activityId);

        return cmd;
    }

    public boolean isFetchAllReportingPeriods() {
        return fetchAllReportingPeriods;
    }

    public void setFetchAllReportingPeriods(boolean fetchAllReportingPeriods) {
        this.fetchAllReportingPeriods = fetchAllReportingPeriods;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GetSites: {");

        if (seekToSiteId != null) {
            sb.append("seektoid=").append(seekToSiteId);
        }
        if (filter != null) {
            sb.append(", filter=").append(filter.toString());
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GetSites getSites = (GetSites) o;

        if (!filter.equals(getSites.filter)) {
            return false;
        }
        if (seekToSiteId != null ? !seekToSiteId.equals(getSites.seekToSiteId) : getSites.seekToSiteId != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = filter.hashCode();
        result = 31 * result + (seekToSiteId != null ? seekToSiteId.hashCode() : 0);
        return result;
    }

    @Override
    public void setOffset(int offset) {
        if (offset != getOffset()) {
            super.setOffset(offset);
            seekToSiteId = null;
        }
    }

    public boolean isFetchPartner() {
        return fetchPartner;
    }

    public void setFetchPartner(boolean fetchPartner) {
        this.fetchPartner = fetchPartner;
    }

    public boolean isFetchLocation() {
        return fetchLocation;
    }

    public void setFetchLocation(boolean fetchLocation) {
        this.fetchLocation = fetchLocation;
    }

    public boolean isFetchDates() {
        return fetchDates;
    }

    public void setFetchDates(boolean fetchDates) {
        this.fetchDates = fetchDates;
    }

    public boolean isFetchComments() {
        return fetchComments;
    }

    public void setFetchComments(boolean fetchComments) {
        this.fetchComments = fetchComments;
    }

    public Integer getSeekToSiteId() {
        return seekToSiteId;
    }

    public void setSeekToSiteId(Integer seekToSiteId) {
        this.seekToSiteId = seekToSiteId;
    }

    public boolean isFetchAttributes() {
        return fetchAttributes;
    }

    public void setFetchAttributes(boolean fetchAttributes) {
        this.fetchAttributes = fetchAttributes;
    }

    public boolean isFetchAllIndicators() {
        return fetchAllIndicators;
    }

    public boolean fetchAnyIndicators() {
        return fetchAllIndicators || (fetchIndicators != null && !fetchIndicators.isEmpty());
    }

    public void setFetchAllIndicators(boolean fetchAllIndicators) {
        this.fetchAllIndicators = fetchAllIndicators;
    }

    public Set<Integer> getFetchIndicators() {
        return fetchIndicators;
    }

    public void setFetchIndicators(Collection<Integer> fetchIndicators) {
        this.fetchIndicators = Sets.newHashSet(fetchIndicators);
    }

    public boolean isFetchAdminEntities() {
        return fetchAdminEntities;
    }

    public void setFetchAdminEntities(boolean fetchAdminEntities) {
        this.fetchAdminEntities = fetchAdminEntities;
    }

}
