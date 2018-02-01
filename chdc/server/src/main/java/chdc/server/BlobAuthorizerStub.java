package chdc.server;

import org.activityinfo.store.spi.BlobAuthorizer;

/**
 * Created by alex on 1-2-18.
 */
class BlobAuthorizerStub implements BlobAuthorizer {
    @Override
    public boolean isOwner(int userId, String blobId) {
        return true;
    }
}
