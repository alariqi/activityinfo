package org.activityinfo.store.query.impl;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.spi.FormCatalog;

/**
 * Scans directly over the FormStorages, with no filtering or permissions applied.
 */
public class FormStorageScanner implements FormScanner {

    private final FormCatalog catalog;

    public FormStorageScanner(FormCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public FormScan scan(ResourceId formId) {
        return new FormStorageScan(catalog.getForm(formId).get());
    }
}
