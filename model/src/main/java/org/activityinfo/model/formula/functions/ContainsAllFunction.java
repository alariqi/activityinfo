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
package org.activityinfo.model.formula.functions;

import com.google.common.collect.Sets;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldType;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.NullFieldValue;
import org.activityinfo.model.type.primitive.BooleanFieldValue;
import org.activityinfo.model.type.primitive.BooleanType;

import java.util.List;
import java.util.Set;

/**
 * @author yuriyz on 9/3/14.
 */
public class ContainsAllFunction extends FormulaFunction {

    public static final ContainsAllFunction INSTANCE = new ContainsAllFunction();

    private ContainsAllFunction() {
    }

    @Override
    public String getId() {
        return "containsAll";
    }

    @Override
    public String getLabel() {
        return "Includes All";
    }

    @Override
    public BooleanFieldValue apply(List<FieldValue> arguments) {
        if (arguments.get(0).equals(NullFieldValue.INSTANCE)) {
            return BooleanFieldValue.FALSE;
        }

        Set<ResourceId> arg1 = Sets.newHashSet(Casting.toSet(arguments.get(0)));
        boolean arg1IsEmpty = arg1.isEmpty();
        for (int i = 1; i < arguments.size(); i++) {
            Set<ResourceId> nextArg = Casting.toSet(arguments.get(i));
            if (!nextArg.isEmpty() && arg1IsEmpty) {
                return BooleanFieldValue.FALSE;
            }
            arg1.removeAll(nextArg);
        }

        return BooleanFieldValue.valueOf(arg1.isEmpty());
    }

    @Override
    public FieldType resolveResultType(List<FieldType> argumentTypes) {
        return BooleanType.INSTANCE;
    }
}
