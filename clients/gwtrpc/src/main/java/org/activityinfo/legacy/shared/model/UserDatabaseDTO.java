package org.activityinfo.legacy.shared.model;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.activityinfo.legacy.shared.model.LockedPeriodDTO.HasLockedPeriod;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import java.util.*;

/**
 * UserDatabase Data Transfer Object.
 *
 * @author Alex Bertram
 */
@JsonAutoDetect(JsonMethod.NONE)
public final class UserDatabaseDTO extends BaseModelData implements EntityDTO, HasLockedPeriod, ProvidesKey {

    public static final int MAX_NAME_LENGTH = 255;

    public static final String MY_PARTNER_ID = "myPartnerId";

    private CountryDTO country;
    private List<PartnerDTO> partners = new ArrayList<PartnerDTO>(0);
    private List<ActivityDTO> activities = new ArrayList<>(0);
    private Set<LockedPeriodDTO> lockedPeriods = new HashSet<LockedPeriodDTO>(0);
    private List<ProjectDTO> projects = new ArrayList<>(0);
    private List<FolderDTO> folders = new ArrayList<>(0);

    public final static String ENTITY_NAME = "UserDatabase";

    public UserDatabaseDTO() {
    }

    public UserDatabaseDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    /**
     * @return this UserDatabase's id
     */
    @Override @JsonProperty @JsonView(DTOViews.List.class)
    public int getId() {
        return (Integer) get("id");
    }

    /**
     * Sets this UserDatabase's id
     */
    public void setId(int id) {
        set("id", id);
    }

    /**
     * @return the name of this UserDatabase
     */
    @Override 
    @JsonProperty 
    @JsonView({DTOViews.Schema.class, DTOViews.List.class})
    public String getName() {
        return get("name");
    }

    /**
     * Sets the name of this UserDatabase
     */
    public void setName(String name) {
        set("name", name);
    }

    /**
     * Sets the name of this UserDatabase's owner
     *
     * @param ownerName
     */
    public void setOwnerName(String ownerName) {
        set("ownerName", ownerName);
    }

    /**
     * @return the name of this UserDatabase's owner
     */
    public String getOwnerName() {
        return get("ownerName");
    }

    /**
     * Sets the email of this UserDatabase's owner
     */
    public void setOwnerEmail(String ownerEmail) {
        set("ownerEmail", ownerEmail);
    }

    /**
     * @return the email of this UserDatabase's owner
     */
    public String getOwnerEmail() {
        return get("ownerEmail");
    }

    /**
     * Sets the full, descriptive name of this UserDatabase
     */
    public void setFullName(String fullName) {
        set("fullName", fullName);
    }

    /**
     * Gets the full, descriptive name of this UserDatabase
     */
    @JsonProperty("description") 
    @JsonView(DTOViews.Schema.class)
    public String getFullName() {
        return get("fullName");
    }

    /**
     * @return this list of ActivityDTOs that belong to this UserDatabase
     */
    @JsonProperty 
    @JsonView(DTOViews.Schema.class)
    public List<ActivityDTO> getActivities() {
        return activities;
    }

    /**
     * @param activities sets the list of Activities in this UserDatabase
     */
    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

    /**
     * @return the Country in which this UserDatabase is set
     */
    @JsonProperty 
    @JsonView(DTOViews.Schema.class)
    public CountryDTO getCountry() {
        return country;
    }

    /**
     * Sets the Country to which this UserDatabase belongs
     */
    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    /**
     * @return the list of Partners who belong to this UserDatabase
     */
    @JsonProperty @JsonView(DTOViews.Schema.class)
    public List<PartnerDTO> getPartners() {
        return partners;
    }

    public List<FolderDTO> getFolders() {
        return folders;
    }

    /**
     * Sets the list of Partners who belong to this UserDatabase
     */
    public void setPartners(List<PartnerDTO> partners) {
        this.partners = partners;
    }

    @JsonProperty @JsonView(DTOViews.Schema.class)
    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDTO> projects) {
        this.projects = projects;
    }

    /**
     * Sets the permission of the current user to view all partner's data in
     * this UserDatabase.
     */
    public void setViewAllAllowed(boolean value) {
        set("viewAllAllowed", value);
    }

    /**
     * @return true if the client receiving the DTO is authorized to view data
     * from all partners in this UserDatabase.
     */
    public boolean isViewAllAllowed() {
        return (Boolean) get("viewAllAllowed", false);
    }

    /**
     * Sets the permission of the current user to edit data on behalf of the
     * Partner in this UserDatabase to which the current user belongs.
     */
    public void setEditAllowed(boolean allowed) {
        set("editAllowed", allowed);
    }

    /**
     * @return true if the client receiving the DTO is authorized to edit data
     * for their Partner in this UserDatabase
     */
    @JsonProperty @JsonView(DTOViews.Schema.class)
    public boolean isEditAllowed() {
        return get("editAllowed", false);
    }

    /**
     * Sets the permission of the current user to design this UserDatabase.
     */
    public void setDesignAllowed(boolean allowed) {
        set("designAllowed", allowed);
    }

    /**
     * @return true if the client receiving the DTO is authorized to design
     * (change indicators, etc) this UserDatabase
     */
    @JsonProperty @JsonView(DTOViews.Schema.class)
    public boolean isDesignAllowed() {
        return get("designAllowed", false);
    }

    /**
     * Sets the permission of the current user to edit data in this UserDatabase
     * on behalf of all partners.
     */
    public void setEditAllAllowed(boolean value) {
        set("editAllAllowed", value);
    }

    /**
     * @return true if the client receiving the DTO is authorized to edit data
     * for all Partners in this UserDatabase
     */
    @JsonProperty @JsonView(DTOViews.Schema.class)
    public boolean isEditAllAllowed() {
        return get("editAllAllowed", false);
    }

    /**
     * @return true if current user is allowed to make changes to user
     * permissions on behalf of the Partner to which they belong
     */
    public boolean isManageUsersAllowed() {
        return get("manageUsersAllowed", false);
    }

    /**
     * Sets the permission of the current user to make changes to user
     * permissions on behalf of the Partner to which they belong in this
     * UserDatabase.
     */
    public void setManageUsersAllowed(boolean allowed) {
        set("manageUsersAllowed", allowed);
    }

    /**
     * @return true if the current user is allowed to make changes to user
     * permissions on behalf of all Partners in this UserDatabase
     */
    public boolean isManageAllUsersAllowed() {
        return get("manageAllUsersAllowed", false);
    }

    /**
     * Sets the permission of the current user to modify user permissions for
     * this UserDatabase on behalf of all Partners in this UserDatabase
     */
    public void setManageAllUsersAllowed(boolean allowed) {
        set("manageAllUsersAllowed", allowed);
    }

    public Optional<PartnerDTO> getDefaultPartner() {
        return getDefaultPartner(getPartners());
    }

    public static Optional<PartnerDTO> getDefaultPartner(Collection<PartnerDTO> partners) {
        for (PartnerDTO partner : partners) {
            if (PartnerDTO.DEFAULT_PARTNER_NAME.equals(partner.getName())) {
                return Optional.of(partner);
            }
        }
        return Optional.absent();
    }

    /**
     * @return the Partner of the UserDatabase to which the client belongs
     */
    public PartnerDTO getMyPartner() {
        return getPartnerById(getMyPartnerId());
    }

    public boolean hasPartnerId() {
        return (get(MY_PARTNER_ID) != null);
    }

    /**
     * @return the id of the Partner to which the client belongs
     */
    public int getMyPartnerId() {
        return hasPartnerId() ? (Integer) get(MY_PARTNER_ID) : 0;
    }

    /**
     * Sets the id of the Partner to which the current user belongs
     */
    public void setMyPartnerId(int partnerId) {
        set(MY_PARTNER_ID, partnerId);
    }

    /**
     * @return true if the client owns this UserDatabase
     */
    @JsonProperty("owned") @JsonView(DTOViews.Schema.class)
    public boolean getAmOwner() {
        return get("amOwner", false);
    }

    /**
     * Sets the flag to determine whether the current user is the owner of this
     * database.
     */
    public void setAmOwner(boolean value) {
        set("amOwner", value);
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    /**
     * Searches this UserDatabase's list of Partners for the PartnerDTO with the
     * given id.
     *
     * @return the matching UserDatabaseDTO or null if no matches
     */
    public PartnerDTO getPartnerById(int id) {
        for (PartnerDTO partner : getPartners()) {
            if (partner.getId() == id) {
                return partner;
            }
        }
        return null;
    }

    public ActivityDTO getActivityById(int id) {
        for (ActivityDTO activity : getActivities()) {
            if (activity.getId() == id) {
                return activity;
            }
        }
        return null;
    }

    @Override
    public String getKey() {
        return "db" + getId();
    }

    public void setLockedPeriods(Set<LockedPeriodDTO> lockedPeriods) {
        this.lockedPeriods = lockedPeriods;
    }

    @Override @JsonProperty @JsonView(DTOViews.Schema.class)
    public Set<LockedPeriodDTO> getLockedPeriods() {
        return lockedPeriods;
    }

    @Override
    public Set<LockedPeriodDTO> getEnabledLockedPeriods() {
        Set<LockedPeriodDTO> lockedPeriods = new HashSet<LockedPeriodDTO>(0);

        for (LockedPeriodDTO lcokedPeriod : getLockedPeriods()) {
            if (lcokedPeriod.isEnabled()) {
                lockedPeriods.add(lcokedPeriod);
            }
        }

        return lockedPeriods;
    }

    public ProjectDTO getProjectById(int value) {
        for (ProjectDTO project : getProjects()) {
            if (value == project.getId()) {
                return project;
            }
        }

        return null;
    }


    public FolderDTO getFolderById(int folderId) {
        for (FolderDTO folder : folders) {
            if(folder.getId() == folderId) {
                return folder;
            }
        }
        return null;
    }

    public boolean isAllowedToEdit(SiteDTO site) {
        if (isEditAllAllowed()) {
            return true;
        } else if (isEditAllowed()) {
            return getMyPartnerId() == site.getPartnerId();
        } else {
            return false;
        }
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

    public List<PartnerDTO> getAllowablePartners() {
        Set<PartnerDTO> result = Sets.newHashSet();
        Optional<PartnerDTO> defaultPartner = getDefaultPartner();

        if (defaultPartner.isPresent()) {
            result.add(defaultPartner.get());
        }

        if (isEditAllAllowed()) {
            result.addAll(getPartners());
        } else if (hasPartnerId()) {
            result.add(getMyPartner());
        } else {
            // if the user has no specific rights, they may not
            // have any options to set the partner
        }
        return Lists.newArrayList(result);
    }

}
