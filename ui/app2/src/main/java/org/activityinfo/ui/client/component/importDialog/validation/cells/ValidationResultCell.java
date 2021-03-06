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
package org.activityinfo.ui.client.component.importDialog.validation.cells;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.component.importDialog.model.strategy.ColumnAccessor;
import org.activityinfo.ui.client.component.importDialog.model.validation.ValidatedRow;
import org.activityinfo.ui.client.component.importDialog.model.validation.ValidationResult;
import org.activityinfo.ui.client.component.importDialog.validation.ValidationPageStyles;

/**
 * @author yuriyz on 5/5/14.
 */
public class ValidationResultCell extends AbstractCell<ValidatedRow> {

    public static interface Templates extends SafeHtmlTemplates {

        public static final Templates INSTANCE = GWT.create(Templates.class);

        @Template("<div class='{0}' title='{1}' style='white-space: pre-line;'>&nbsp;{2}</div>")
        public SafeHtml html(String style, String tooltip, String text);
    }

    private final ColumnAccessor accessor;
    private final int columnIndex;

    public ValidationResultCell(ColumnAccessor accessor, int columnIndex) {
        super();
        this.accessor = accessor;
        this.columnIndex = columnIndex;
    }

    @Override
    public void render(Context context, ValidatedRow data, SafeHtmlBuilder sb) {
        ValidationResult result = data.getResult(columnIndex);
        SafeHtml safeHtml = Templates.INSTANCE.html(style(result), tooltip(result), accessor.getValue(data.getSourceRow()));
        sb.append(correctNewLineCharacter(safeHtml));
    }

    private static SafeHtml correctNewLineCharacter(SafeHtml safeHtml) {
        return SafeHtmlUtils.fromTrustedString(safeHtml.asString().replace("#013;", "&#013;"));
    }

    private static String tooltip(ValidationResult result) {
        boolean isGreen = result.getState() == ValidationResult.State.OK ||
                (result.getState() == ValidationResult.State.CONFIDENCE && result.getConfidence() == 1);
        boolean isPercentConfidence = result.getState() == ValidationResult.State.CONFIDENCE && result.getConfidence() < 1;
        if (isGreen) {
            return I18N.CONSTANTS.importPerfectMatchTooltip();
        } else if (isPercentConfidence) {
            int confidencePercent = (int) (result.getConfidence() * 100);
            return I18N.MESSAGES.importValidationCellTooltip(result.getTargetValue(), confidencePercent);
        } else if (result.getState() == ValidationResult.State.ERROR && !Strings.isNullOrEmpty(result.getTypeConversionErrorMessage())) {
            return result.getTypeConversionErrorMessage() + " " + I18N.CONSTANTS.failedToMatchValue();
        }
        return I18N.CONSTANTS.failedToMatchValue();
    }

    private static String style(ValidationResult result) {
        if (result != null) {
            switch (result.getState()) {
                case OK:
                    return ValidationPageStyles.INSTANCE.stateOk();
                case CONFIDENCE:
                    if (result.getConfidence() == 1) {
                        return ValidationPageStyles.INSTANCE.stateOk();
                    }
                    if (!result.isPersistable()) {
                        return ValidationPageStyles.INSTANCE.stateError();
                    }
                    return ValidationPageStyles.INSTANCE.stateConfidence();
                case ERROR:
                case MISSING:
                    if (!result.hasReferenceMatch()) {
                        return ValidationPageStyles.INSTANCE.stateError();
                    }
            }
        }
        return "";
    }
}
