package org.activityinfo.store.query.shared.join;

import org.activityinfo.store.query.impl.join.ForeignKey;
import org.activityinfo.store.query.shared.FilterLevel;
import org.activityinfo.store.query.shared.Slot;

/**
 * Lookup key for JoinLinks in a Collection Scan
 */
public class ReferenceJoinKey {

  private final FilterLevel filterLevel;
  private final Slot<ForeignKey> foreignKey;
  private final Slot<PrimaryKeyMap> primaryKeyMap;

  public ReferenceJoinKey(FilterLevel filterLevel, Slot<ForeignKey> foreignKey, Slot<PrimaryKeyMap> primaryKeyMap) {
    this.filterLevel = filterLevel;
    this.foreignKey = foreignKey;
    this.primaryKeyMap = primaryKeyMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReferenceJoinKey that = (ReferenceJoinKey) o;

    if (filterLevel != that.filterLevel) return false;
    if (!foreignKey.equals(that.foreignKey)) return false;
    return primaryKeyMap.equals(that.primaryKeyMap);

  }

  @Override
  public int hashCode() {
    int result = filterLevel.hashCode();
    result = 31 * result + foreignKey.hashCode();
    result = 31 * result + primaryKeyMap.hashCode();
    return result;
  }
}
