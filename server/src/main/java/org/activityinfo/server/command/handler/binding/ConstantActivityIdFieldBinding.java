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
package org.activityinfo.server.command.handler.binding;

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formula.ConstantNode;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.resource.ResourceId;

import java.util.Collections;
import java.util.List;

public class ConstantActivityIdFieldBinding extends ActivityIdFieldBinding {

    private ResourceId id;

    public ConstantActivityIdFieldBinding(int id) {
        this.id = CuidAdapter.activityFormClass(id);
    }

    @Override
    public List<ColumnModel> getColumnQuery(FormTree formTree) {
        return Collections.singletonList(new ColumnModel().setFormula(new ConstantNode(id.toString())).as(CLASS_ID_COLUMN));
    }

    @Override
    public List<ColumnModel> getTargetColumnQuery(ResourceId targetFormId) {
        return Collections.singletonList(new ColumnModel().setFormula(new ConstantNode(id.toString())).as(CLASS_ID_COLUMN));
    }
}
