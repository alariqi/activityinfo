package org.activityinfo.ui.client.table.model;

import org.activityinfo.model.resource.ResourceId;

public interface SliderUpdater {

    TableUpdater getTableUpdater(ResourceId formId);
}
