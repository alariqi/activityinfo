/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.grid;

import org.sigmah.client.dispatch.AsyncMonitor;

import com.extjs.gxt.ui.client.data.ModelData;

public interface GridView<PresenterT extends GridPresenter, ModelT extends ModelData> {

    public void setActionEnabled(String actionId, boolean enabled);

    public void confirmDeleteSelected(ConfirmCallback callback);

    public ModelT getSelection();

    public AsyncMonitor getDeletingMonitor();

    public AsyncMonitor getSavingMonitor();


}
