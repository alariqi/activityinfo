package chdc.frontend.client.theme;

import chdc.theme.client.base.field.Css3RadioAppearance;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public class ChdcRadioAppearance extends Css3RadioAppearance {

    public interface ChdcRadioResources extends Css3RadioAppearance.Css3RadioResources {
        @ClientBundle.Source({
                "chdc/theme/client/base/field/Css3ValueBaseField.gss",
                "chdc/theme/client/base/field/Css3CheckBox.gss",
                "chdc/theme/client/base/field/Css3Radio.gss",
                "radio.gss"})
        ChdcRadioStyle style();
    }

    public interface ChdcRadioStyle extends Css3RadioStyle {
    }

    public ChdcRadioAppearance() {
        this(GWT.<ChdcRadioResources> create(ChdcRadioResources.class));
    }

    public ChdcRadioAppearance(Css3CheckBoxResources resources) {
        super(resources);
        type = "radio";
    }
}
