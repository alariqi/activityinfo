package org.activityinfo.store;

import org.activityinfo.store.spi.FormStorageProvider;

public interface TransactionalStorageProvider extends FormStorageProvider {

    void begin();

    void commit();

    void rollback();


}
