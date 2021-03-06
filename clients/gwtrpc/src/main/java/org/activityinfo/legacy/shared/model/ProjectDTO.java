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

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.activityinfo.legacy.shared.model.LockedPeriodDTO.HasLockedPeriod;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import java.util.HashSet;
import java.util.Set;

@JsonAutoDetect(JsonMethod.NONE)
public final class ProjectDTO extends BaseModelData implements EntityDTO, HasLockedPeriod {

    private Set<LockedPeriodDTO> lockedPeriods = new HashSet<>(0);
    private UserDatabaseDTO userDatabase;

    public static final String ENTITY_NAME = "Project";

    public ProjectDTO() {
        super();
    }

    public ProjectDTO(int id, String name) {
        super();

        setId(id);
        setName(name);
    }

    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public int getId() {
        return (Integer) get(ID_PROPERTY);
    }

    public void setId(int id) {
        set(ID_PROPERTY, id);
    }

    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
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
    @JsonView(DTOViews.Schema.class)
    public String getDescription() {
        return get("description");
    }

    @Override
    public Set<LockedPeriodDTO> getLockedPeriods() {
        return lockedPeriods;
    }

    public void setLockedPeriods(Set<LockedPeriodDTO> lockedPeriods) {
        this.lockedPeriods = lockedPeriods;
    }

    public void setUserDatabase(UserDatabaseDTO database) {
        this.userDatabase = database;
    }

    public UserDatabaseDTO getUserDatabase() {
        return userDatabase;
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