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

import org.activityinfo.legacy.shared.reports.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.legacy.shared.reports.model.clustering.AutomaticClustering;
import org.activityinfo.legacy.shared.reports.model.clustering.Clustering;
import org.activityinfo.legacy.shared.reports.model.clustering.NoClustering;
import org.activityinfo.legacy.shared.reports.model.labeling.ArabicNumberSequence;
import org.activityinfo.legacy.shared.reports.model.labeling.LabelSequence;
import org.activityinfo.legacy.shared.reports.model.labeling.LatinAlphaSequence;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlTransient;

public abstract class PointMapLayer extends AbstractMapLayer {

    private LabelSequence labelSequence;
    private Clustering clustering = new NoClustering();

    @XmlElementRefs({@XmlElementRef(type = ArabicNumberSequence.class),
            @XmlElementRef(type = LatinAlphaSequence.class)})
    public LabelSequence getLabelSequence() {
        return labelSequence;
    }

    public void setLabelSequence(LabelSequence labelSequence) {
        this.labelSequence = labelSequence;
    }

    @XmlTransient
    public boolean isClustered() {
        return clustering.isClustered();
    }

    @XmlElementRefs({@XmlElementRef(type = AdministrativeLevelClustering.class),
            @XmlElementRef(type = NoClustering.class),
            @XmlElementRef(type = AutomaticClustering.class)})
    public Clustering getClustering() {
        return clustering;
    }

    public void setClustering(Clustering clustering) {
        this.clustering = clustering;
    }

}
