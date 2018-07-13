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
package org.activityinfo.ui.client.store;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.ReferenceValue;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.ObservableTree;
import org.activityinfo.promise.Maybe;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecordTreeLoader implements ObservableTree.TreeLoader<
    RecordTreeLoader.NodeKey,
    RecordTreeLoader.Node,
    RecordTree> {

    public abstract class NodeKey {
        public abstract Observable<Node> get(FormStore formStore);
    }

    private class RecordKey extends NodeKey {
        @Nonnull
        private final RecordRef ref;

        public RecordKey(@Nonnull RecordRef ref) {
            this.ref = ref;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RecordKey recordKey = (RecordKey) o;

            return ref.equals(recordKey.ref);
        }

        @Override
        public int hashCode() {
            return ref.hashCode();
        }

        @Override
        public String toString() {
            return "RecordKey{" + ref + "}";
        }

        @Override
        public Observable<Node> get(FormStore formStore) {
            return formStore.getRecord(ref).transform(result -> new RecordNode(ref, result));
        }
    }

    public abstract class Node {

        protected abstract Iterable<NodeKey> getChildren();

        protected abstract void addTo(Map<RecordRef, Maybe<FormInstance>> records, Multimap<RecordTree.ParentKey, FormInstance> subRecords);
    }

    private class RecordNode extends Node {
        private final RecordRef ref;
        private final FormClass formClass;
        private final Maybe<FormInstance> record;

        public RecordNode(RecordRef ref, Maybe<FormRecord> record) {
            this.ref = ref;
            this.formClass = formTree.getFormClass(ref.getFormId());
            this.record = record.transform(r -> FormInstance.toFormInstance(formClass, r));
        }

        @Override
        public Iterable<NodeKey> getChildren() {
            Set<NodeKey> children = new HashSet<>();
            if(record.isVisible()) {
                findChildren(children, formClass, record.get());
            }
            return children;
        }

        @Override
        protected void addTo(Map<RecordRef, Maybe<FormInstance>> records, Multimap<RecordTree.ParentKey, FormInstance> subRecords) {
            records.put(ref, record);
        }
    }


    private void findChildren(Set<NodeKey> children, FormClass schema, FormInstance record) {
        // Add referenced records
        for (FieldValue value : record.getFieldValueMap().values()) {
            if (value instanceof ReferenceValue) {
                for (RecordRef recordRef : ((ReferenceValue) value).getReferences()) {
                    children.add(new RecordKey(recordRef));
                }
            }
        }
    }


    private final FormStore formStore;
    private final FormTree formTree;
    private final RecordRef rootRecordRef;

    public RecordTreeLoader(FormStore formStore, FormTree formTree, RecordRef rootRecordRef) {
        this.formStore = formStore;
        this.formTree = formTree;
        this.rootRecordRef = rootRecordRef;
    }

    @Override
    public NodeKey getRootKey() {
        return new RecordKey(rootRecordRef);
    }

    @Override
    public Observable<Node> get(NodeKey nodeKey) {
        return nodeKey.get(formStore);
    }

    @Override
    public Iterable<NodeKey> getChildren(Node node) {
        return node.getChildren();
    }

    @Override
    public RecordTree build(Map<NodeKey, Observable<Node>> nodes) {

        Map<RecordRef, Maybe<FormInstance>> records = new HashMap<>();
        Multimap<RecordTree.ParentKey, FormInstance> subRecords = HashMultimap.create();

        for (Observable<Node> node : nodes.values()) {
            node.get().addTo(records, subRecords);
        }
        return new RecordTree(formTree, rootRecordRef, records);
    }

    @Override
    public String toString() {
        return "RecordTree{" + rootRecordRef + "}";
    }
}
