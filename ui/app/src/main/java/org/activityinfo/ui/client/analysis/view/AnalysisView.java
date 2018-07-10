/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.analysis.view;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.HasTitle;
import org.activityinfo.ui.client.analysis.viewModel.AnalysisViewModel;
import org.activityinfo.ui.client.header.HasFixedHeight;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.client.VDomWidget;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.Arrays;

public class AnalysisView implements IsWidget, HasTitle, HasFixedHeight {

    private VDomWidget container;
    private AnalysisViewModel model;
    private PivotTableView pivotTableView;

    public AnalysisView(FormStore formStore, AnalysisViewModel model) {
        this.model = model;
        container = new VDomWidget();

        AnalysisBundle.INSTANCE.getStyles().ensureInjected();

        VTree formSelection = H.div();

        VTree page = new PageBuilder()
                .breadcrumbs(model.getTitle().transform(t ->
                        Arrays.asList(new Breadcrumb(I18N.CONSTANTS.reports(), UriUtils.fromSafeConstant("#")),
                                new Breadcrumb(t, UriUtils.fromSafeConstant("#")))))
                .heading(model.getTitle())
                .body(formSelection)
                .build();

        container.update(page);
    }


    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public Observable<String> getTitle() {
        return model.getTitle();
    }
}
