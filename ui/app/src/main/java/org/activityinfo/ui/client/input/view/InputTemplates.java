package org.activityinfo.ui.client.input.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface InputTemplates extends XTemplates {

    InputTemplates TEMPLATES = GWT.create(InputTemplates.class);

    @XTemplate("<h1>{heading}</h1>")
    SafeHtml formHeading(SafeHtml heading);

    @XTemplate("<h3>{label}</h3>")
    SafeHtml fieldHeading(String label);

    @XTemplate("<h3><span>{label}*</span><span class=\"forminput__field__required\">{requiredText}</span></h3>")
    SafeHtml requiredFieldHeading(String label, String requiredText);

    @XTemplate("<p class=\"forminput__field__description\">{description}</p>")
    SafeHtml fieldDescription(String description);
}
