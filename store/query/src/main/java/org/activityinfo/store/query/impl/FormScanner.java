package org.activityinfo.store.query.impl;

import org.activityinfo.model.resource.ResourceId;

public interface FormScanner {

    FormScan scan(ResourceId formId);

}
