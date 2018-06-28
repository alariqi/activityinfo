package org.activityinfo.ui.client.search;

import com.google.gwt.user.client.ui.IsWidget;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;
import org.activityinfo.ui.vdom.shared.tree.VWidget;

public class SearchWidget extends VWidget {

    private FormStore formStore;

    public SearchWidget(FormStore formStore) {
        this.formStore = formStore;
    }

    @Override
    public IsWidget createWidget() {
        return new SearchBox(formStore);
    }

    @Override
    protected VTree render() {
         return new VNode(HtmlTag.DIV, PropMap.withClasses("search__container"));
    }
}
