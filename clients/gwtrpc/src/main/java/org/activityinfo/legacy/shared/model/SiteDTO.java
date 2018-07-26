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
package org.activityinfo.legacy.shared.model;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.collect.Maps;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;

/**
 * Projection DTO for the
 * {@link org.activityinfo.server.database.hibernate.entity.Site} domain object,
 * including its
 * {@link org.activityinfo.server.database.hibernate.entity.Location Location},
 * and {@link org.activityinfo.server.database.hibernate.entity.ReportingPeriod
 * ReportingPeriod} totals
 *
 * @author Alex Bertram
 */
public final class SiteDTO extends BaseModelData implements EntityDTO, HasAdminEntityValues {

    public static final String ENTITY_NAME = "Site";
    public static final String LOCATION_NAME_PROPERTY = "locationName";

    // ensure that serializer/deserializer is generated for LocalDate
    private LocalDate date;

    private Date dateCreated;

    private Map<String, List<String>> attributeDisplayMap;

    public SiteDTO() {
        set("name", " ");
    }

    /**
     * Constucts an empty SiteDTO with the given id
     *
     * @param id the siteId
     */
    public SiteDTO(int id) {
        setId(id);
        set("name", " ");
    }

    /**
     * Constructs a shallow copy of the given SiteDTO instance
     *
     * @param site the object to copy
     */
    public SiteDTO(SiteDTO site) {
        super(site.getProperties());
    }

    /**
     * Sets this site's id
     *
     */
    public void setId(int id) {
        set("id", id);
    }

    /**
     * @return this site's id
     */
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public ResourceId getInstanceId() {
        return CuidAdapter.cuid(CuidAdapter.SITE_DOMAIN, getId());
    }


    /**
     * @return the id of the Activity to which this Site belongs
     */
    public int getActivityId() {
        return (Integer) get("activityId");
    }


    public ResourceId getFormClassId() {
        return CuidAdapter.cuid(CuidAdapter.ACTIVITY_DOMAIN, getActivityId());
    }

    /**
     * Sets the id of Activity to which this Site belongs
     *
     */
    public void setActivityId(int id) {
        set("activityId", id);
    }

    /**
     * @return the beginning of work at this Site
     */
    public LocalDate getDate1() {
        return get("date1");
    }

    /**
     * Sets the beginning of work at this Site
     */
    public void setDate1(Date date1) {
        if (date1 == null) {
            set("date1", null);
        } else {
            set("date1", new LocalDate(date1));
        }
    }

    public void setDate1(LocalDate date1) {
        set("date1", date1);
    }

    /**
     * @return the end of work at this Site
     */
    public LocalDate getDate2() {
        return get("date2");
    }

    /**
     * Sets the end of work at this Site
     */
    public void setDate2(Date date2) {
        if (date2 == null) {
            set("date2", null);
        } else {
            set("date2", new LocalDate(date2));
        }
    }

    public void setDate2(LocalDate date2) {
        set("date2", date2);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getTimeEdited() {
        return (Long) get("timeEdited");
    }

    public void setTimeEdited(long timeEdited) {
        set("timeEdited", timeEdited);
    }

    public void setTimeEdited(double timeEdited) {
        setTimeEdited((long) timeEdited);
    }

    public void setReportingPeriodId(int id) {
        set("reportingPeriodId", id);
    }

    public Integer getReportingPeriod() {
        return get("reportingPeriodId");
    }

    public boolean isLinked() {
        return get("linked", false);
    }

    public void setLinked(boolean linked) {
        set("linked", linked);
    }

    /**
     * @return the name of the Partner who owns this Site
     */
    public String getPartnerName() {
        PartnerDTO partner = getPartner();
        if (partner == null) {
            return null;
        }

        return partner.getName();
    }

    public String getProjectName() {
        return getProject() == null ? "" : getProject().getName();
    }

    /**
     * @return the instance of the Partner who owns this Site
     */
    public PartnerDTO getPartner() {
        return get("partner");
    }

    /**
     * Sets the partner who owns this Site
     */
    public void setPartner(PartnerDTO partner) {
        set("partner", partner);
    }

    /**
     * Sets the name of Location of this Site. See
     * {@link org.activityinfo.server.database.hibernate.entity.Location#getName()}
     *
     * @param name the name of the location.
     */
    public void setLocationName(String name) {
        set(LOCATION_NAME_PROPERTY, name);
    }

    /**
     * @return the name of the Location of the Site
     */
    public String getLocationName() {
        return get(LOCATION_NAME_PROPERTY);
    }

    /**
     * @return the "axe routier" on which the Location of the Site lies
     */
    public String getLocationAxe() {
        return get("locationAxe");
    }

    /**
     * Sets the axe routier on which the Location of the Site lies
     */
    public void setLocationAxe(String name) {
        set("locationAxe", name);
    }

    public void setAdminEntity(int levelId, AdminEntityDTO value) {
        set(AdminLevelDTO.getPropertyName(levelId), value);
    }

    @Override
    public AdminEntityDTO getAdminEntity(int levelId) {
        return get(AdminLevelDTO.getPropertyName(levelId));
    }

    public Object getAdminEntityName(int levelId) {
        AdminEntityDTO entity = getAdminEntity(levelId);
        if (entity == null) {
            return null;
        }

        return entity.getName();
    }

    /**
     * Sets the X (longitudinal) coordinate of this Site
     *
     * @param x the longitude, in degrees
     */
    public void setX(Double x) {
        set("x", x);
    }

    /**
     * @return the X (longitudinal) coordinate of this Site, or null if a
     * coordinate has not been set.
     */
    public Double getX() {
        return get("x");
    }

    /**
     * @return the Y (latitudinal) coordinate of this Site in degrees, or null
     * if a coordinate has not been set.
     */
    public Double getY() {
        return get("y");
    }

    public Double getLatitude() {
        return getY();
    }

    public Double getLongitude() {
        return getX();
    }

    /**
     * Sets the Y (latitudinal) coordinate of this Site in degrees
     *
     * @param y latitude in degrees
     */
    public void setY(Double y) {
        set("y", y);
    }

    /**
     * @return true if this Site has non-null x and y coordinates.
     */
    public boolean hasCoords() {
        return get("x") != null && get("y") != null;
    }

    public boolean hasLatLong() {
        return hasCoords();
    }

    /**
     * Sets the value for the given Attribute of this Site
     *
     * @param attributeId the Id of the attribute
     * @param value
     */
    public void setAttributeValue(int attributeId, Boolean value) {
        set(AttributeDTO.getPropertyName(attributeId), value);
    }

    /**
     * Sets the (total) value of the given Indicator of this Site
     *
     * @param indicatorId the Id of the indicator
     * @param value       the total value for all ReportingPeriods
     */
    public void setIndicatorValue(int indicatorId, Object value) {
        set(IndicatorDTO.getPropertyName(indicatorId), value);
    }

    /**
     * @param indicatorId
     * @return the total value of the given indicator for this Site, across all
     * ReportingPeriods
     */
    public <T> T getIndicatorValue(int indicatorId) {
        return get(IndicatorDTO.getPropertyName(indicatorId));
    }

    /**
     * @param indicator
     * @return the total value of the given indicator for this Site, across all
     * ReportingPeriods
     */
    public <T> T getIndicatorValue(IndicatorDTO indicator) {
        return getIndicatorValue(indicator.getId());
    }

    /**
     * Returns indicator value if it has type double, otherwise null.
     *
     * @param indicator indicator dto
     * @return indicator value if it has type double, otherwise null
     */
    public Double getIndicatorDoubleValue(IndicatorDTO indicator) {
        return getIndicatorDoubleValue(indicator.getId());
    }

    /**
     * Returns indicator value if it has type double, otherwise null.
     *
     * @param indicatorId indicator id
     * @return indicator value if it has type double, otherwise null
     */
    public Double getIndicatorDoubleValue(int indicatorId) {
        Object value = getIndicatorValue(indicatorId);
        return value instanceof Double ? (Double) value : null;
    }

    /**
     * Sets the plain text comments for this Site
     *
     * @param comments comments in plain-text format
     */
    public void setComments(String comments) {
        set("comments", comments);
    }

    /**
     * @return the plain-text comments for this Site
     */
    public String getComments() {
        return get("comments");
    }

    /**
     * @param attributeId
     * @return the value of the given attribute for this Site
     */
    public boolean getAttributeValue(int attributeId) {
        return get(AttributeDTO.getPropertyName(attributeId), false);
    }

    /**
     * Tests equality based on id
     *
     * @param o
     * @return true if the given Site has the same Id as this Site
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SiteDTO siteModel = (SiteDTO) o;
        if (getId() != siteModel.getId()) {
            return false;
        }
        if (!Objects.equals(get("reportingPeriodId"), siteModel.get("reportingPeriodId"))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * @return true if this Site has a non-null ID
     */
    public boolean hasId() {
        return get("id") != null;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    public ProjectDTO getProject() {
        return get("project");
    }

    public void setProject(ProjectDTO project) {
        set("project", project);
    }

    /**
     * Returns the location in the format of [LocationName (LocationAxe)]
     */
    public String getPrettyLocationName() {
        return getLocationName() + getLocationAxe() != null ? " (" + getLocationAxe() + ")" : "";
    }

    public void setLocationId(int locationId) {
        set("locationId", locationId);
    }

    public void setLocation(LocationDTO location) {
        setLocationId(location.getId());
        setLocationName(location.getName());
        setLocationAxe(location.getAxe());
        setY(location.getLatitude());
        setX(location.getLongitude());
        setWorkflowStatusId(location.getWorkflowStatusId());

        for (String admin : location.getPropertyNames()) {
            if (admin.startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
                int id = Integer.parseInt(admin.substring(1, admin.length()));
                setAdminEntity(id, location.getAdminEntity(id));
            }
        }
    }

    /**
     * Returns a new location constructed from flattened properties of this site
     */
    public LocationDTO getLocation() {
        LocationDTO location = new LocationDTO();
        location.setId(getLocationId());
        location.setName(getLocationName());
        location.setAxe(getLocationAxe());
        location.setLatitude(getY());
        location.setLongitude(getX());
        location.setWorkflowStatusId(getWorkflowStatusId());

        for (AdminEntityDTO entity : getAdminEntities().values()) {
            location.setAdminEntity(entity.getLevelId(), entity);
        }

        return location;
    }

    public String getWorkflowStatusId() {
        return get("workflowStatusId");
    }

    public void setWorkflowStatusId(String workflowStatusId) {
        set("workflowStatusId", workflowStatusId);
    }

    public Integer getLocationId() {
        return get("locationId");
    }

    @Override
    public String getName() {
        return null;
    }

    public int getPartnerId() {
        return getPartner().getId();
    }

    public Map<Integer, AdminEntityDTO> getAdminEntities() {
        Map<Integer, AdminEntityDTO> map = Maps.newHashMap();
        for (String property : getPropertyNames()) {
            if (property.startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
                int levelId = AdminLevelDTO.levelIdForPropertyName(property);
                map.put(levelId, (AdminEntityDTO) get(property));
            }
        }
        return map;
    }

    public SiteDTO copy() {
        SiteDTO copy = new SiteDTO();
        copy.setProperties(this.getProperties());
        return copy;
    }

    public void addDisplayAttribute(String groupName, String attributeName) {
        if (groupName != null && attributeName != null) {
            if (attributeDisplayMap == null) {
                attributeDisplayMap = new HashMap<>();
            }

            List<String> groupValues = attributeDisplayMap.get(groupName);
            if (groupValues == null) {
                groupValues = new ArrayList<>();
                attributeDisplayMap.put(groupName, groupValues);
            }

            groupValues.add(attributeName);
        }
    }

    public Map<String, List<String>> getAttributeDisplayMap() {
        return attributeDisplayMap;
    }

    public boolean hasAttributeDisplayMap() {
        return attributeDisplayMap != null && !attributeDisplayMap.isEmpty();
    }

    @JsonIgnore
    @Override
    public Map<String, Object> getProperties() {
        return super.getProperties();
    }

    @JsonIgnore
    @Override
    public Collection<String> getPropertyNames() {
        return super.getPropertyNames();
    }

}
