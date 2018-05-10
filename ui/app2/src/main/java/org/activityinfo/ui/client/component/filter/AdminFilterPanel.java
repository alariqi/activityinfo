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
package org.activityinfo.ui.client.component.filter;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.command.Filter;
import org.activityinfo.legacy.shared.model.AdminEntityDTO;
import org.activityinfo.ui.client.component.filter.FilterToolBar.ApplyFilterEvent;
import org.activityinfo.ui.client.component.filter.FilterToolBar.ApplyFilterHandler;
import org.activityinfo.ui.client.component.filter.FilterToolBar.RemoveFilterEvent;
import org.activityinfo.ui.client.component.filter.FilterToolBar.RemoveFilterHandler;
import org.activityinfo.ui.client.dispatch.Dispatcher;
import org.activityinfo.ui.client.style.legacy.icon.IconImageBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * UI Component for editing Admin restrictions on a
 * {@link org.activityinfo.legacy.shared.command.Filter}
 *
 * @author Alex Bertram
 */
public class AdminFilterPanel extends ContentPanel implements FilterPanel {

    private final Dispatcher dispatcher;
    private TreeStore<AdminEntityDTO> store;
    private AdminTreeLoader loader;

    private TreePanel<AdminEntityDTO> tree;

    private Filter baseFilter = new Filter();
    private Filter value = new Filter();

    private FilterToolBar filterToolBar;

    @Inject
    public AdminFilterPanel(Dispatcher service) {
        this.dispatcher = service;

        initializeComponent();

        createAdminEntitiesTree();
        createFilterToolBar();
    }

    private void createAdminEntitiesTree() {
        tree = new TreePanel<AdminEntityDTO>(store) {

            @Override
            protected String register(AdminEntityDTO m) {
                String result = super.register(m);

                // at this point we know the TreeNode has been created
                // so we can set the check state
                if (value.getRestrictions(DimensionType.AdminLevel).contains(m.getId())) {
                    tree.setChecked(m, true);
                }

                return result;
            }
        };

        tree.setCheckable(true);
        tree.setCheckNodes(TreePanel.CheckNodes.BOTH);
        tree.setCheckStyle(TreePanel.CheckCascade.CHILDREN);

        tree.setDisplayProperty("name");
        tree.getStyle().setNodeCloseIcon(null);
        tree.getStyle().setNodeOpenIcon(null);
        tree.setStateful(true);
        tree.addCheckListener(new CheckChangedListener<AdminEntityDTO>() {

            @Override
            public void checkChanged(CheckChangedEvent<AdminEntityDTO> event) {
                filterToolBar.setApplyFilterEnabled(!tree.getCheckedSelection().isEmpty());
            }
        });

        add(tree);
    }

    private void initializeComponent() {
        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeadingText(I18N.CONSTANTS.filterByGeography());
        this.setIcon(IconImageBundle.ICONS.filter());

        loader = new AdminTreeLoader(dispatcher);
        store = new TreeStore<AdminEntityDTO>(loader) {

            @Override
            protected void onBeforeLoad(LoadEvent le) {
                if (isCollapsed()) {
                    le.setCancelled(true);
                }

                if (!le.isCancelled() && !this.fireEvent(BeforeDataChanged, this.createStoreEvent())) {
                    le.setCancelled(true);
                }
            }
        };
    }

    private void createFilterToolBar() {
        filterToolBar = new FilterToolBar();

        filterToolBar.addApplyFilterHandler(new ApplyFilterHandler() {
            @Override
            public void onApplyFilter(ApplyFilterEvent deleteEvent) {
                applyFilter();
            }
        });

        filterToolBar.addRemoveFilterHandler(new RemoveFilterHandler() {
            @Override
            public void onRemoveFilter(RemoveFilterEvent deleteEvent) {
                removeFilter();
            }
        });

        setTopComponent(filterToolBar);
    }

    /**
     * @return the list of AdminEntityDTOs that user has selected with which the
     * filter should be restricted
     */
    public List<AdminEntityDTO> getSelection() {
        List<AdminEntityDTO> checked = tree.getCheckedSelection();
        List<AdminEntityDTO> selected = new ArrayList<>();

        for (AdminEntityDTO entity : checked) {
            selected.add(entity);
        }
        return selected;
    }

    public void setSelection(int id, boolean select) {

        for (ModelData model : tree.getStore().getAllItems()) {
            if (model instanceof AdminEntityDTO && ((AdminEntityDTO) model).getId() == id) {
                tree.setChecked((AdminEntityDTO) model, select);
            }
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Filter> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    private void applyFilter() {
        List<AdminEntityDTO> selection = getSelection();
        value = new Filter();
        for (AdminEntityDTO entity : selection) {
            value.addRestriction(DimensionType.AdminLevel, entity.getId());
        }
        ValueChangeEvent.fire(this, value);
        filterToolBar.setRemoveFilterEnabled(true);
    }

    private void removeFilter() {
        for (AdminEntityDTO entity : tree.getCheckedSelection()) {
            tree.setChecked(entity, false);
        }

        value = new Filter();
        ValueChangeEvent.fire(this, value);
        filterToolBar.setRemoveFilterEnabled(false);
    }

    @Override
    public Filter getValue() {
        return value;
    }

    @Override
    public void setValue(Filter value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Filter value, boolean fireEvents) {
        this.value = new Filter();
        this.value.addRestriction(DimensionType.AdminLevel, value.getRestrictions(DimensionType.AdminLevel));
        applyInternalState();

        filterToolBar.setApplyFilterEnabled(false);
        filterToolBar.setRemoveFilterEnabled(value.isRestricted(DimensionType.AdminLevel));

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public void applyBaseFilter(final Filter providedFilter) {
        Filter filter = new Filter(providedFilter);
        filter.clearRestrictions(DimensionType.AdminLevel);

        if (baseFilter == null || !baseFilter.equals(filter)) {
            loader.setFilter(filter);
            loader.load();

            baseFilter = filter;
        }
    }

    @Override
    protected void afterExpand() {
        super.afterExpand();
        loader.load();
    }

    private void applyInternalState() {
        for (AdminEntityDTO treeNode : tree.getStore().getAllItems()) {
            tree.setChecked(treeNode, value.getRestrictions(DimensionType.AdminLevel).contains(treeNode.getId()));
        }
    }

}
