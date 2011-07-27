/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.dialog.SaveChangesCallback;
import org.sigmah.client.page.common.dialog.SavePromptMessageBox;
import org.sigmah.client.page.common.toolbar.UIActions;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteFormPage extends SiteForm {


    private ToolBar toolBar;


    private void createToolBar() {

        SelectionListener listener = new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                if (presenter != null) {
                    presenter.onUIAction(ce.getComponent().getItemId());
                }
            }
        };

        Button gridButton = new Button(I18N.CONSTANTS.returnToGrid(), IconImageBundle.ICONS.table(), listener);
        gridButton.setItemId(UIActions.gotoGrid);
        toolBar.add(gridButton);

        Button saveButton = new Button(I18N.CONSTANTS.save(), IconImageBundle.ICONS.save(), listener);
        saveButton.setItemId(UIActions.save);
        toolBar.add(saveButton);

        Button discardButton = new Button(I18N.CONSTANTS.discardChanges(),
                IconImageBundle.ICONS.cancel(), listener);
        discardButton.setItemId(UIActions.cancel);
        toolBar.add(discardButton);

        setTopComponent(toolBar);

    }


    public void promptSaveChanges(final SaveChangesCallback callback) {

        final SavePromptMessageBox box = new SavePromptMessageBox();

        box.show(callback);

    }

}
