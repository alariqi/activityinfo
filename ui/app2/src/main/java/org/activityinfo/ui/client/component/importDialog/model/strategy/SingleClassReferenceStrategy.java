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

import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.ui.client.component.importDialog.model.ImportModel;

import java.util.List;
import java.util.Map;

/**
 * Strategy for importing reference fields whose ranges include a single class. In this
 * case, we also know the class of the value that will be imported, but we still need to
 * deal with transient references that might include more complex reference fields, such
 * as point-in-hierarchies.
 *
 * <p>In the example below, both the Partner and Localite fields would be accepted by this
 * strategy, but we consider each unique field as a single target site, even if it is
 * referenced by multiple fields in the tree.
 *
 * <p><We should actually be distinguishing between fields of equivalent ranges but different
 * semantic meaning, but we don't yet have a use case that poses this problem.
 *
 * <pre>
 * ..Partner = <Partner>
 * ..............Name
 * ..............Full Name
 * ..Localité = [Localité]
 * ...............Name
 * ...............Alternate Name
 * ...............Geographic coordinates
 * ...............Administrative Unit = [Zone de Santé  | Territoire | District | Province]
 * ....................................................Name
 * .....................................[Province]
 * .......................................Name
 * .....................................[Zone de Santé]
 * .......................................Name
 *.......................................Province = [Province]
 * ....................................................Name
 * .....................................[District]
 * .......................................Name
 * .......................................Province = [Province]
 * .....................................[Territoire]
 * .......................................Name
 * .......................................District = [District]
 * ....................................................Name
 * ....................................................Province = [Province]
 * .................................................................Name
 * </pre>
 *
 * In the example above, we allow the user to match columns to "Partner Name" and.or "Partner Full Name"
 * which are used to match the Partner entity.
 *
 */
public class SingleClassReferenceStrategy implements FieldImportStrategy {


    @Override
    public boolean accept(FormTree.Node fieldNode) {
        return fieldNode.isReference() && fieldNode.getRange().size() == 1;
    }

    @Override
    public List<ImportTarget> getImportSites(FormTree.Node node) {
        return new SingleClassTargetBuilder(node).getTargets();
    }

    @Override
    public SingleClassImporter createImporter(FormTree.Node node, Map<TargetSiteId, ColumnAccessor> mappings, ImportModel model) {
        return new SingleClassTargetBuilder(node).newImporter(mappings);
    }
}
