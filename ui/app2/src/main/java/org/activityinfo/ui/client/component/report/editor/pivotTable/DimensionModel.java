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
package org.activityinfo.ui.client.component.report.editor.pivotTable;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.model.AdminLevelDTO;
import org.activityinfo.legacy.shared.model.AttributeGroupDTO;
import org.activityinfo.legacy.shared.reports.model.AdminDimension;
import org.activityinfo.legacy.shared.reports.model.AttributeGroupDimension;
import org.activityinfo.legacy.shared.reports.model.DateDimension;
import org.activityinfo.legacy.shared.reports.model.Dimension;
import org.activityinfo.model.date.DateUnit;

import java.util.List;
import java.util.Set;

public class DimensionModel extends BaseModelData {

    private final Dimension dimension;

    public DimensionModel(String name) {
        setName(name);
        this.dimension = null;
    }

    public DimensionModel(Dimension dimension, String name) {
        super();
        this.dimension = dimension;
        setName(name);
    }

    public DimensionModel(DimensionType type, String name) {
        this.dimension = new Dimension(type);
        setName(name);
    }

    public DimensionModel(DateUnit unit) {
        this.dimension = new DateDimension(unit);
        switch (unit) {
            case YEAR:
                setName(I18N.CONSTANTS.year());
                break;
            case QUARTER:
                setName(I18N.CONSTANTS.quarter());
                break;
            case MONTH:
                setName(I18N.CONSTANTS.month());
                break;
            case WEEK_MON:
                setName(I18N.CONSTANTS.weekMon());
                break;
            default:
                throw new IllegalArgumentException(unit.name());
        }
    }

    public DimensionModel(AdminLevelDTO level) {
        this.dimension = new AdminDimension(level.getId());
        setName(level.getName());
    }

    public DimensionModel(AttributeGroupDTO attributeGroup) {
        this.dimension = new AttributeGroupDimension(attributeGroup.getId());
        setName(attributeGroup.getName());
    }

    public String getCaption() {
        return get("name");
    }

    public void setName(String caption) {
        set("name", caption);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public boolean hasDimension() {
        return dimension != null;
    }


    public static List<DimensionModel> attributeGroupModels(List<AttributeGroupDTO> groups) {
        /*
         * Attribute Groups retain their own identity and ids 
         * by Activity, but once we get to this stage, we treat
         * attribute groups with the same name as the same thing.
         * 
         * This allows user to define attributes across databases
         * and activities through "offline" coordination.
         */
        List<DimensionModel> models = Lists.newArrayList();
        Set<String> groupsAdded = Sets.newHashSet();
        for (AttributeGroupDTO attributeGroup : groups) {

            if (!groupsAdded.contains(attributeGroup.getName())) {
                DimensionModel dimModel = new DimensionModel(attributeGroup);
                models.add(dimModel);
                groupsAdded.add(attributeGroup.getName());
            }
        }
        return models;
    }

    public static List<DimensionModel> adminLevelModels(List<AdminLevelDTO> data) {
        List<DimensionModel> models = Lists.newArrayList();
        for(AdminLevelDTO level : data) {
            models.add(new DimensionModel(level));
        }
        return models;
    }
}
