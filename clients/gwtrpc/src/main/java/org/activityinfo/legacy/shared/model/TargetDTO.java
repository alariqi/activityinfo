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
import org.activityinfo.legacy.shared.validation.Required;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSetter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@JsonAutoDetect(JsonMethod.NONE)
public class TargetDTO extends BaseModelData implements EntityDTO {

    public static final String ENTITY_NAME = "Target";

    private UserDatabaseDTO userDatabase;
    private List<TargetValueDTO> targetValues;

    public TargetDTO() {
        super();
    }

    public TargetDTO(int id, String name) {
        super();
        set(ID_PROPERTY, id);
        set(NAME_PROPERTY, name);
    }

    @Override
    @JsonProperty
    public int getId() {
        return get(ID_PROPERTY, 0);
    }

    public void setId(int id) {
        set("id", id);
    }

    @Override
    @Required
    @JsonProperty
    @Length(max = 255)
    public String getName() {
        return get(NAME_PROPERTY);
    }

    public void setName(String name) {
        set(NAME_PROPERTY, name);
    }

    public void setDescription(String description) {
        set("description", description);
    }

    @JsonProperty
    public String getDescription() {
        return get("description");
    }

    public void setUserDatabase(UserDatabaseDTO database) {
        this.userDatabase = database;
    }

    public UserDatabaseDTO getUserDatabase() {
        return userDatabase;
    }


    @JsonProperty
    public ProjectDTO getProject() {
        return get("project");
    }

    @JsonSetter
    public void setProjectId(Integer id) {
        if(id == null) {
            setProject(null);
        } else {
            ProjectDTO project = new ProjectDTO();
            project.setId(id);
            setProject(project);
        }
    }

    public void setProject(ProjectDTO value) {
        set("project", value);
        if (value != null) { // keep also id, we still stick to id in bindings
            set("projectId", value.getId());
        }
    }

    @JsonProperty
    public PartnerDTO getPartner() {
        return get("partner");
    }

    @JsonSetter
    public void setPartnerId(Integer id) {
        if(id == null) {
            setPartner(null);
        } else {
            PartnerDTO partner = new PartnerDTO();
            partner.setId(id);
            setPartner(partner);
        }
    }

    public void setPartner(PartnerDTO value) {
        set("partner", value);
        if (value != null) { // keep also id, we still stick to id in bindings
            set("partnerId", value.getId());
        }
    }

    public AdminEntityDTO getAdminEntity() {
        return get("adminEntity");
    }

    public void setAdminEntity(AdminEntityDTO value) {
        set("adminEntity", value);
    }



    @Required
    @JsonProperty
    public LocalDate getFromDate() {
        return get("fromDate");
    }

    @JsonSetter
    public void setFromDate(LocalDate date) {
        set("fromDate", date);
    }

    @Required
    @JsonProperty
    public LocalDate getToDate() {
        return get("toDate");
    }

    @JsonSetter
    public void setToDate(LocalDate date) {
        set("toDate", date);
    }

    public void setFromDate(Date date1) {
        if(date1 == null) {
            setFromDate((LocalDate)null);
        } else {
            setFromDate(new LocalDate(date1));
        }
    }

    public void setToDate(Date date2) {
        if(date2 == null) {
            setToDate((LocalDate)null);
        } else {
            setToDate(new LocalDate(date2));
        }
    }

    @JsonProperty
    public List<TargetValueDTO> getTargetValues() {
        return targetValues;
    }

    public void setTargetValues(List<TargetValueDTO> targetValues) {
        this.targetValues = targetValues;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }
}
