package org.activityinfo.server.endpoint.odk;

import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.ReferenceType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.activityinfo.model.legacy.CuidAdapter.LOCATION_FIELD;

public class OdkHelper {

    public static String extractText(Node node) {
        NodeList childNodes = node.getChildNodes();

        if (childNodes.getLength() == 1) {
            Node child = childNodes.item(0);
            if (child.getChildNodes().getLength() == 0 && "#text".equals(child.getNodeName())) {
                return child.getNodeValue();
            }
        }

        return null;
    }

    public static boolean isLocation(FormClass formClass, FormField formField) {
        ResourceId locationFieldId = CuidAdapter.field(formClass.getId(), LOCATION_FIELD);
        return formField.getId().equals(locationFieldId);
    }

    public static ResourceId extractLocationReference(FormField formField) {
        if (formField.getType() instanceof ReferenceType) {
            ReferenceType referenceType = (ReferenceType) formField.getType();
            for (ResourceId locationFormId : referenceType.getRange()) {
                // Check for non Admin Level location reference
                Character domain = locationFormId.getDomain();
                if (!domain.equals(CuidAdapter.ADMIN_LEVEL_DOMAIN)) {
                    return locationFormId;
                }
            }
            return null;
        } else {
            return formField.getId();
        }
    }

}
