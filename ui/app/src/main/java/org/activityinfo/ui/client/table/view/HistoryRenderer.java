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
package org.activityinfo.ui.client.table.view;

import com.google.gwt.i18n.client.DateTimeFormat;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.RecordHistory;
import org.activityinfo.model.form.RecordHistoryEntry;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.Date;
import java.util.Optional;

public class HistoryRenderer {

    private static final DateTimeFormat CHANGE_TIME_FORMAT =
            DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);

    private HistoryRenderer() {}

    public static VTree render(Optional<RecordHistory> history) {
        return history.isPresent() ? entries(history.get()) : emptyState();
    }

    private static VTree emptyState() {
        return new VNode(HtmlTag.DIV, "No selection");
    }

    private static VTree entries(RecordHistory history) {
        if (!history.isAvailable()) {
            return renderNoHistory();
        }
        return new VNode(HtmlTag.DIV, history.getEntries().stream().map(e -> entry(e)));
    }

    private static VTree entry(RecordHistoryEntry e) {
        return new VNode(HtmlTag.DIV, PropMap.withClasses("history__entry"),
                new VNode(HtmlTag.DIV, PropMap.withClasses("history__date"), new VText(formatTime(e))),
                new VNode(HtmlTag.DIV, PropMap.withClasses("history__type"), new VText(formatType(e))),
                new VNode(HtmlTag.DIV, PropMap.withClasses("history__user"), new VText(formatUser(e))));
    }

    private static String formatTime(RecordHistoryEntry e) {
        return CHANGE_TIME_FORMAT.format(new Date(e.getTime() * 1000L));
    }


    private static String formatType(RecordHistoryEntry e) {
        switch (e.getChangeType()) {
            case "created":
                return "Record added";
            default:
            case "update":
                return  "Record modified";
        }
    }

    private static String formatUser(RecordHistoryEntry e) {
        return e.getUserName() + " — " + e.getUserEmail();
    }

    private static VTree renderNoHistory() {
        return new VText(I18N.MESSAGES.siteHistoryNotAvailable());
    }

}
