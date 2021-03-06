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

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * A data transfer object with a one-to-one relationship with a JPA @Entity.
 *
 * @author Alex Bertram
 */
public interface EntityDTO extends DTO, ModelData {

    String ID_PROPERTY = "id";
    String NAME_PROPERTY = "name";
    String SORT_ORDER_PROPERTY = "sortOrder";
    String DATABASE_ID_PROPERTY = "databaseId";

    /**
     * @return the corresponding entity's @Id
     */
    public int getId();

    /**
     * @return the entity's JPA name
     */
    public String getEntityName();

    public String getName();
}
