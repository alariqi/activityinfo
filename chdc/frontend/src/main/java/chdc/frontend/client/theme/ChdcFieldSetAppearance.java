package chdc.frontend.client.theme;

import chdc.theme.client.base.fieldset.Css3FieldSetAppearance;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public class ChdcFieldSetAppearance extends Css3FieldSetAppearance {

    public interface ChdcFieldSetResources extends Css3FieldSetResources {

        @Override
        @ClientBundle.Source({"com/sencha/gxt/theme/base/client/field/FieldSet.gss", "fieldset.gss"})
        ChdcFieldSetStyle css();
    }

    public interface ChdcFieldSetStyle extends Css3FieldSetStyle {
    }

    public ChdcFieldSetAppearance() {
        super(GWT.<ChdcFieldSetResources> create(ChdcFieldSetResources.class));
    }
}
