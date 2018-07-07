package org.activityinfo.ui.client.store;

import com.google.gwt.core.client.testing.StubScheduler;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.form.FormPermissions;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.Cardinality;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.ObservableTree;
import org.activityinfo.observable.Observer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class FormTreeLoaderTest {

    @Test
    public void noFlicker() {

        // If the required forms are all loaded, then the FormTree should never transition to the
        // the loading state

        FormClass form1 = new FormClass(ResourceId.valueOf("FORM1"));
        FormClass form2 = new FormClass(ResourceId.valueOf("FORM2"));

        form1.addField(ResourceId.valueOf("REFFIELD"))
                .setLabel("reference field")
                .setType(new ReferenceType(Cardinality.SINGLE, form2.getId()));

        form2.addField(ResourceId.valueOf("Q"))
                .setLabel("Num households")
                .setType(new QuantityType("households"));

        Map<ResourceId, Observable<FormMetadata>> forms = new HashMap<>();
        forms.put(form1.getId(), Observable.just(FormMetadata.of(1, form1, FormPermissions.owner())));
        forms.put(form2.getId(), Observable.just(FormMetadata.of(1, form2, FormPermissions.owner())));


        FormTreeLoader loader = new FormTreeLoader(form1.getId(), formId -> forms.get(formId));
        StubScheduler scheduler = new StubScheduler();
        Observable<FormTree> observable = new ObservableTree<>(loader, scheduler);

        List<String> changes = new ArrayList<>();
        Observer<FormTree> observer = new Observer<FormTree>() {
            @Override
            public void onChange(Observable<FormTree> observable) {
                if(observable.isLoading()) {
                    changes.add("loading...");
                } else {
                    changes.add("loaded");
                }
            }
        };

        observable.subscribe(observer);

        assertThat(changes, hasItems("loaded"));

    }

}