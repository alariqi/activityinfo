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
package org.activityinfo.legacy.shared.reports.model.layers;

import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.command.Filter;
import org.activityinfo.legacy.shared.reports.model.Dimension;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a graduated symbol layer where indicators are visualized using a
 * circle
 */
public class BubbleMapLayer extends CircledMapLayer {

    private List<Dimension> colorDimensions = new ArrayList<Dimension>();
    private String bubbleColor = "FF0000";
    private String labelColor = "000000";

    public BubbleMapLayer() {
    }

    /**
     * Creates a BubbleMapLayer, initialized with the specified filter
     *
     * @param filter
     */
    public BubbleMapLayer(Filter filter) {
        this();
        setFilter(filter);
    }

    public void addIndicator(int id) {
        getIndicatorIds().add(id);
    }

    public boolean containsIndicatorDimension() {
        for (Dimension dim : colorDimensions) {
            if (dim.getType() == DimensionType.Indicator) {
                return true;
            }
        }
        return false;
    }

    @Deprecated @XmlElement(name = "dimension") @XmlElementWrapper(name = "colors")
    public List<Dimension> getColorDimensions() {
        return colorDimensions;
    }

    @Deprecated
    public void setColorDimensions(List<Dimension> colorDimensions) {
        this.colorDimensions = colorDimensions;

    }

    /**
     * @return the color used to draw the circles
     */
    @XmlElement
    public String getBubbleColor() {
        return bubbleColor;
    }

    /**
     * @param color the color used to draw the circles
     */
    public void setBubbleColor(String color) {
        this.bubbleColor = color;
    }

    /**
     * @return the hex color used to label bubbles (for the text)
     */
    @XmlElement
    public String getLabelColor() {
        return labelColor;
    }

    /**
     * @param labelColor the color to use when labelling bubbles (for the text)
     */
    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    @Override
    public boolean supportsMultipleIndicators() {
        return true;
    }

    @Override
    public String getTypeName() {
        return "Bubble";
    }

    @Override
    public String toString() {
        return "BubbleMapLayer [ indicators=" + getIndicatorIds() + "]";
    }

}
