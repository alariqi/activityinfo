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
package org.activityinfo.ui.client.page.config.design.dialog;

import org.activityinfo.ui.client.dispatch.Dispatcher;
import org.activityinfo.ui.client.widget.dialog.PageSwitcher;
import org.activityinfo.ui.client.widget.dialog.WizardPage;

/**
 * @author yuriyz on 11/13/2014.
 */
public class NewDbPageSwitcher implements PageSwitcher {

    private final NewDbTypePage typePage;
    private final NewDbDetailsPage detailsPage;
    private final NewDbExistingListPage existingListPage;

    WizardPage currentPage = null;
    WizardPage previousPage = null;

    public NewDbPageSwitcher(Dispatcher dispatcher, NewDbDialogData dialogData) {
        this.detailsPage = new NewDbDetailsPage(dispatcher, dialogData);
        this.existingListPage = new NewDbExistingListPage(dispatcher, dialogData);
        this.typePage = new NewDbTypePage(dialogData);
    }

    @Override
    public WizardPage getNext() {
        previousPage = currentPage;
        if (currentPage == null) {
            currentPage = typePage;
        } else if (currentPage.equals(typePage)) {

            if (typePage.createNewDb()) {
                detailsPage.showCopyOptions(false);
                currentPage = detailsPage;
            } else {
                currentPage = existingListPage;
            }
        } else if (currentPage.equals(existingListPage)) {
            detailsPage.showCopyOptions(true);
            currentPage = detailsPage;
        }
        return currentPage;
    }

    @Override
    public WizardPage getPrevious() {
        currentPage = previousPage;

        if (previousPage.equals(detailsPage)) {
            if (typePage.createNewDb()) {
                previousPage = typePage;
            } else {
                previousPage = existingListPage;
            }
        } else if (previousPage.equals(existingListPage)) {
            previousPage = typePage;
        } else if (previousPage.equals(typePage)) {
            previousPage = null;
        }

        return currentPage;
    }

    @Override
    public WizardPage getCurrentPage() {
        return currentPage;
    }

    @Override
    public boolean hasPrevious() {
        return previousPage != null;
    }

    @Override
    public boolean hasNext() {
        return !detailsPage.equals(currentPage);
    }

    @Override
    public boolean isFinishPage() {
        return detailsPage.equals(currentPage);
    }

    public NewDbTypePage getTypePage() {
        return typePage;
    }

    public NewDbDetailsPage getDetailsPage() {
        return detailsPage;
    }

    public NewDbExistingListPage getExistingListPage() {
        return existingListPage;
    }
}
