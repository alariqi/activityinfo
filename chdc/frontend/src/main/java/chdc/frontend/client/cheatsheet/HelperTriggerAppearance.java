package chdc.frontend.client.cheatsheet;

import chdc.theme.client.base.field.Css3TriggerFieldAppearance;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

/**
 * Overrides the ComboBox trigger field appearance to show help icon instead
 * of downward arrow.
 */
class HelperTriggerAppearance extends Css3TriggerFieldAppearance {


    public interface Resources extends Css3TriggerFieldResources {

        @Override
        @Source({
                "chdc/theme/client/base/field/Css3ValueBaseField.gss",
                "chdc/theme/client/base/field/Css3TextField.gss",
                "help.gss"})
        HelperTriggerStyles style();

        @Source("help.png")
        ImageResource helpTrigger();
    }

    public interface HelperTriggerStyles extends Css3TriggerFieldStyle {
    }


    public HelperTriggerAppearance() {
        super(GWT.create(Resources.class));
    }

}
