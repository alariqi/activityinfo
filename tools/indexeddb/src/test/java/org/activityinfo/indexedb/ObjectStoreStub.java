/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.indexedb;

import org.activityinfo.promise.Promise;

import java.util.*;

import static org.activityinfo.json.Json.toJson;


public class ObjectStoreStub<T> {

    private final String name;
    private final ObjectStoreOptions options;
    private final KeyPath keyPath;
    private final boolean autoIncrement;

    private int nextId = 1;

    private TreeMap<ObjectKey, T> objectMap = new TreeMap<>();

    public ObjectStoreStub(String name, ObjectStoreOptions options) {
        this.name = name;
        this.options = options;
        this.keyPath = KeyPath.from(options);
        this.autoIncrement = options.isAutoIncrement();
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

    class Transaction implements IDBObjectStore<T> {

        private boolean readwrite;

        public Transaction(boolean readwrite) {
            this.readwrite = readwrite;
        }
        @Override
        public void put(T value) {
            checkReadWrite();
            ObjectKey key;
            if(autoIncrement) {
                key = new ObjectKey(nextId++);
            } else {
                key = keyPath.buildKey(toJson(value));
            }
            objectMap.put(key, value);
        }

        @Override
        public void put(int key, T object) {
            put(new ObjectKey(key), object);
        }

        @Override
        public void put(String key, T value) {
            put(new ObjectKey(key), value);
        }

        @Override
        public void put(String[] key, T object) {
            put(new ObjectKey(key), object);
        }

        void put(ObjectKey key, T value) {
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
        public Promise<T> get(int key) {
            return get(new ObjectKey(key));
        }


        @Override
        public void delete(String lowerBound, String upperBound) {
            ObjectKey lowerKey = new ObjectKey(lowerBound);
            ObjectKey upperKey = new ObjectKey(upperBound);

            Set<ObjectKey> toDelete = new HashSet<>();

            for (ObjectKey objectKey : objectMap.keySet()) {
                if(lowerKey.compareTo(objectKey) <= 0 ||
                   objectKey.compareTo(upperKey) <= 0) {
                    toDelete.add(objectKey);
                }
            }

            for (ObjectKey objectKey : toDelete) {
                objectMap.remove(objectKey);
            }
        }

        private void delete(ObjectKey key) {
            checkReadWrite();
            objectMap.remove(key);
        }

        @Override
        public void delete(int key) {
            delete(new ObjectKey(key));
        }

        private Promise<T> get(ObjectKey key) {
            return Promise.resolved(objectMap.get(key));
        }


        @Override
        public void openCursor(IDBCursorCallback<T> callback) {
            Cursor<T> cursor = new Cursor<T>(this, objectMap.entrySet().iterator(), callback);
            cursor.run();
        }

        @Override
        public void delete(String key) {
            delete(new ObjectKey(key));
        }

        @Override
        public void openCursor(String lowerBound, String upperBound, IDBCursorCallback callback) {

            NavigableMap<ObjectKey, T> range = objectMap.subMap(
                new ObjectKey(lowerBound), true,
                new ObjectKey(upperBound), true);
            Iterator<Map.Entry<ObjectKey, T>> it = range.entrySet().iterator();

            Cursor<T> cursor = new Cursor<T>(this, it, callback);
            cursor.run();
        }
    }
}
