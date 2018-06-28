package org.activityinfo.ui.client.base.avatar;

import com.google.gwt.safehtml.shared.SafeHtml;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public interface Avatar {

    SafeHtml render();

    VTree renderTree();
}
