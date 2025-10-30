package com.biit.infographic.core.engine.content;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Component
public class FormContent {

    private static final String FORM_PARAMETER = "SUBMIT";
    private static final String TIME_ATTRIBUTE = "TIME";
    private static final String TIME_ATTRIBUTE_PATTERN = "HH:mm";
    private static final String DATE_ATTRIBUTE = "DATE";
    private static final String DATE_ATTRIBUTE_PATTERN = "dd/MM/yyyy";

    public void setUserVariableValues(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm)
            throws ElementDoesNotExistsException {
        if (parameters == null) {
            return;
        }
        for (Parameter parameter : parameters) {
            if (Objects.equals(parameter.getName(), FORM_PARAMETER)) {
                InfographicEngineLogger.debug(this.getClass(), "Processing parameter '#{}%{}%{}#'.",
                        parameter.getType(), parameter.getName(), parameter.getAttributes());
                // Search for any variable defined in the parameters
                for (String attribute : parameter.getAttributes().keySet()) {
                    //#FORM%SUBMIT%DATE#
                    try {
                        if (Objects.equals(attribute, TIME_ATTRIBUTE)) {
                            //Angular sends the time on UTC, that is set on the submittedAt field.
                            parameter.getAttributes().put(attribute, droolsSubmittedForm.getSubmittedAt()
                                    .atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                                    .format(DateTimeFormatter.ofPattern(TIME_ATTRIBUTE_PATTERN)));
                        } else if (Objects.equals(attribute, DATE_ATTRIBUTE)) {
                            //Angular sends the time on UTC, that is set on the submittedAt field.
                            parameter.getAttributes().put(attribute, droolsSubmittedForm.getSubmittedAt()
                                    .atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                                    .format(DateTimeFormatter.ofPattern(DATE_ATTRIBUTE_PATTERN)));
                        }
                    } catch (Exception e) {
                        InfographicEngineLogger.errorMessage(this.getClass(), e);
                    }
                }
            }
        }
    }
}
