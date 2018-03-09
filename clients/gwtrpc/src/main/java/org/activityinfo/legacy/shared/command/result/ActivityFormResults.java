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
package org.activityinfo.legacy.shared.command.result;

import org.activityinfo.legacy.shared.model.ActivityFormDTO;
import org.activityinfo.legacy.shared.model.AttributeGroupDTO;

import java.util.List;

public class ActivityFormResults extends ListResult<ActivityFormDTO> {

    public ActivityFormResults() {
    }

    public ActivityFormResults(List<ActivityFormDTO> data) {
        super(data);
    }

    /**
     * Retrieves the name of the AttributeGroup from the Schema graph,
     * or returns "" if the attribute group cannot be found in the
     * loaded schema.
     */
    public String getAttributeGroupNameSafe(int attributeGroupId) {
        AttributeGroupDTO group = getAttributeGroupById(attributeGroupId);
        if (group == null) {
            return "";
        } else {
            return group.getName();
        }
    }


    public AttributeGroupDTO getAttributeGroupById(int attributeGroupId) {
        for (ActivityFormDTO activity : getData()) {
            AttributeGroupDTO group = activity.getAttributeGroupById(attributeGroupId);
            if (group != null) {
                return group;
            }
        }
        return null;
    }
}
