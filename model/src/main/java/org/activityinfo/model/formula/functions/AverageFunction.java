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

public class AverageFunction extends StatFunction {

    public static final AverageFunction INSTANCE = new AverageFunction();

    @Override
    public String getId() {
        return "average";
    }

    @Override
    public String getLabel() {
        return "average";
    }

    @Override
    public double compute(double[] values, int start, int end) {
        double sum = 0;
        int count = 0;
        for (int i = start; i < end; i++) {
            double value = values[i];
            if(!Double.isNaN(value)) {
                sum += value;
                count++;
            }
        }
        return sum / (double)count;
    }
}
