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
package org.activityinfo.geoadmin.merge2.model;

import com.google.common.base.Optional;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.ObservableSet;

import java.util.*;

/**
 * Set of explicit {@link org.activityinfo.geoadmin.merge2.model.InstanceMatch}es
 * between <em>source</em> form instances and <em>target</em> instances.
 * 
 * <p>This {@link org.activityinfo.observable.ObservableSet} implementation ensures that 
 * all source instances are matched to at most one target instance, and that all
 * target instances are matched to at most one source instance.</p>
 */
public class InstanceMatchSet extends ObservableSet<InstanceMatch> {
    
    private Map<ResourceId, InstanceMatch> map = new HashMap<>();
    private Set<InstanceMatch> set = new HashSet<>();
    
    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public Set<InstanceMatch> asSet() {
        return Collections.unmodifiableSet(set);
    }

    /**
     * Adds the given {@code InstanceMatch} to the set, and removes any existing matches between
     * the given {@code match}'s source or target instance ids.
     * 
     * <p>Registered {@link org.activityinfo.observable.SetObserver}s are notified of the addition
     * as well as removes of any existing matches linked to the added match's source or target instances.</p>
     * 
     */
    public void add(InstanceMatch match) {
        if(!set.contains(match)) {
            removeMatchesWith(match.getSourceId());
            removeMatchesWith(match.getTargetId());

            if(match.getSourceId().isPresent()) {
                map.put(match.getSourceId().get(), match);
            }
            if(match.getTargetId().isPresent()) {
                map.put(match.getTargetId().get(), match);
            }
            set.add(match);
            fireAdded(match);
        }
    }

    private void removeMatchesWith(Optional<ResourceId> id) {
        if(id.isPresent()) {
            InstanceMatch match = map.remove(id);
            if (match != null) {
                set.remove(match);
                fireRemoved(match);
            }
        }
    }

    /**
     * Removes the given match between target and source instances from the set, notifying
     * any registered listeners 
     * @param match
     */
    public void remove(InstanceMatch match) {
        boolean removed = set.remove(match);
        map.remove(match.getTargetId());
        map.remove(match.getSourceId());
        if(removed) {
            fireRemoved(match);
        }
    }

    /**
     * Returns a match involving the given {@code resourceId} if one is present.
     */
    public Optional<InstanceMatch> find(ResourceId resourceId) {
        return Optional.fromNullable(map.get(resourceId));
    }
}
