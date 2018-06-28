package org.activityinfo.ui.client.table.model;

import org.activityinfo.model.resource.ResourceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TableSliderModel {

    private ResourceId rootFormId;

    private Map<ResourceId, TableModel> tableMap;

    public TableSliderModel(ResourceId rootFormId) {
        this.rootFormId = rootFormId;
        this.tableMap = new HashMap<>();
    }

    public Optional<TableModel> getTableModelIfPresent(ResourceId formId) {
        return Optional.ofNullable(tableMap.get(formId));
    }
}
