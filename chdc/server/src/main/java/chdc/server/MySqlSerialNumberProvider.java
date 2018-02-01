package chdc.server;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.spi.SerialNumberProvider;

public class MySqlSerialNumberProvider implements SerialNumberProvider {
    @Override
    public int next(ResourceId formId, ResourceId fieldId, String prefix) {
        throw new UnsupportedOperationException();
    }
}
