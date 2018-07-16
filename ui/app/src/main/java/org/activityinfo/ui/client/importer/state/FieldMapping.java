package org.activityinfo.ui.client.importer.state;

import java.util.Set;

public interface FieldMapping {

    String getFieldId();

    Set<String> getMappedColumnIds();
}
