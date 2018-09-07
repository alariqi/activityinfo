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
package org.activityinfo.ui.client.component.importDialog.model.strategy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.activityinfo.model.formTree.FieldPath;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;

import java.util.List;
import java.util.Map;

public class SingleClassTargetBuilder {

    private final FormTree.Node rootField;
    private final TargetCollector targetCollector;

    public SingleClassTargetBuilder(FormTree.Node referenceField) {
        rootField = referenceField;
        targetCollector = new TargetCollector(referenceField);
    }

    public List<ImportTarget> getTargets() {
        return targetCollector.getTargets();
    }

    public SingleClassImporter newImporter(Map<TargetSiteId, ColumnAccessor> mappings) {
        List<ColumnAccessor> sourceColumns = Lists.newArrayList();
        Map<FieldPath, Integer> referenceValues = targetCollector.getPathMap(mappings, sourceColumns);
        List<FieldImporterColumn> fieldImporterColumns = targetCollector.fieldImporterColumns(mappings);

        ResourceId rangeClassId = Iterables.getOnlyElement(rootField.getRange());

        return new SingleClassImporter(rangeClassId, rootField.getField().isRequired(),
                sourceColumns, referenceValues, fieldImporterColumns, rootField.getFieldId());
    }
}