package org.activityinfo.indexedb;

import org.activityinfo.json.Json;
import org.activityinfo.json.JsonObject;
import org.activityinfo.json.JsonValue;
import org.activityinfo.promise.Promise;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class ObjectStoreStub<T> {

    private final String name;
    private final ObjectStoreOptions options;
    private final KeyPath keyPath;

    private TreeMap<ObjectKey, T> objectMap = new TreeMap<>();

    public ObjectStoreStub(String name, ObjectStoreOptions options) {
        this.name = name;
        this.options = options;
        this.keyPath = KeyPath.from(options);
    }

    Upgrade upgrade() {
        return new Upgrade();
    }

    public IDBObjectStore transaction(String mode) {
        return new Transaction(mode.equals(IDBDatabase.READWRITE));
    }

    private class Upgrade implements IDBObjectStoreUpgrade {

        @Override
        public void createIndex(String indexName, String keyPath, IndexOptions indexOptions) {
            // TODO
        }
    }

    private class Transaction implements IDBObjectStore<T> {

        private boolean readwrite;

        public Transaction(boolean readwrite) {
            this.readwrite = readwrite;
        }
        @Override
        public void put(T value) {
            checkReadWrite();
            objectMap.put(keyPath.buildKey(Json.toJson(value).getAsJsonObject()), value);
        }

        @Override
        public void put(String key, T value) {
            put(new ObjectKey(key), value);
        }

        @Override
        public void put(String[] key, T object) {
            put(new ObjectKey(key), object);
        }

        private void put(ObjectKey key, T value) {
            checkReadWrite();
            objectMap.put(key, value);
        }

        private void checkReadWrite() {
            if(!readwrite) {
                throw new IllegalStateException("The transaction is read-only.");
            }
        }

        @Override
        public Promise<T> get(String key) {
            return get(new ObjectKey(key));
        }

        @Override
        public Promise<T> get(String[] keys) {
            return get(new ObjectKey(keys));
        }

        @Override
        public void delete(String[] key) {
            checkReadWrite();
            objectMap.remove(new ObjectKey(key));
        }

        private Promise<T> get(ObjectKey key) {
            return Promise.resolved(objectMap.get(key));
        }

        @Override
        public void openCursor(String[] lowerBound, String[] upperBound, IDBCursorCallback callback) {

            NavigableMap<ObjectKey, T> range = objectMap.subMap(
                new ObjectKey(lowerBound), true,
                new ObjectKey(upperBound), true);
            Iterator<Map.Entry<ObjectKey, T>> it = range.entrySet().iterator();

            Cursor<T> cursor = new Cursor<T>(it, callback);
            cursor.run();
        }
    }
}
