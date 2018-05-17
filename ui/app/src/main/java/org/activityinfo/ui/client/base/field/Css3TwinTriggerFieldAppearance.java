package org.activityinfo.ui.client.base.field;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.cell.core.client.form.TwinTriggerFieldCell;
import com.sencha.gxt.core.client.dom.XElement;

public class Css3TwinTriggerFieldAppearance extends Css3TriggerFieldAppearance implements TwinTriggerFieldCell.TwinTriggerFieldAppearance {

    @Override
    public boolean twinTriggerIsOrHasChild(XElement parent, Element target) {
        return false;
    }

    @Override
    public void onTwinTriggerOver(XElement parent, boolean over) {
    }

    @Override
    public void onTwinTriggerClick(XElement parent, boolean click) {
    }
}