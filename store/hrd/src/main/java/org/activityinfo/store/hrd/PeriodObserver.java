package org.activityinfo.store.hrd;

import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.time.PeriodType;
import org.activityinfo.store.hrd.entity.FormRecordEntity;
import org.activityinfo.store.spi.CursorObserver;
import org.activityinfo.store.spi.SubFormPatch;

public class PeriodObserver implements CursorObserver<FormRecordEntity> {

    private static final String PROPERTY_NAME = SubFormPatch.PERIOD_FIELD_ID.asString();
    private final PeriodType periodType;
    private final CursorObserver<FieldValue> observer;

    public PeriodObserver(FormClass formClass, CursorObserver<FieldValue> observer) {
        periodType = formClass.getSubFormKind().getPeriodType();
        this.observer = observer;
    }

    @Override
    public void onNext(FormRecordEntity value) {
        Object property = value.getFieldValues().getProperty(PROPERTY_NAME);
        if(property instanceof String) {
            observer.onNext(periodType.parseString((String) property));
        } else {
            observer.onNext(periodType.fromSubFormKey(value.getRecordRef()));
        }
    }

    @Override
    public void done() {
        observer.done();
    }
}
