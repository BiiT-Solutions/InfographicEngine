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

import com.biit.appointment.core.models.AppointmentDTO;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.engine.content.value.ValueCalculator;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.providers.AppointmentProvider;
import com.biit.infographic.core.providers.UserProvider;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;


@Component
public class AppointmentContent {

    private static final String TEMPLATE_PARAMETER = "TEMPLATE";
    private static final String DURATION_TIME_OPERATION = "DURATION_TIME";
    private static final String APPOINTMENT_STARTING_TIME_HOUR = "STARTING_TIME_HOUR";
    private static final String APPOINTMENT_ENDING_TIME_HOUR = "ENDING_TIME_HOUR";
    private static final String TIME_FORMAT = "HH:mm";


    private static final int TEMPLATE_NAME_POSITION = 0;

    private final AppointmentProvider appointmentProvider;
    private final UserProvider userProvider;
    //Only for testing purposes. Emulate the current time.
    private ZonedDateTime dateToCheck;

    private final ValueCalculator valueCalculator;

    public AppointmentContent(AppointmentProvider appointmentProvider,
                              UserProvider userProvider, ValueCalculator valueCalculator) {
        this.appointmentProvider = appointmentProvider;
        this.userProvider = userProvider;
        this.valueCalculator = valueCalculator;
    }

    public void setAppointmentValues(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm, String timezone)
            throws ElementDoesNotExistsException {
        if (parameters == null) {
            return;
        }

        //Gets the current or next appointment from a template where the current user is an attendee.
        for (Parameter parameter : parameters) {
            if (Objects.equals(parameter.getName(), TEMPLATE_PARAMETER)) {
                InfographicEngineLogger.debug(this.getClass(), "Processing parameter '#{}%{}%{}#'.",
                        parameter.getType(), parameter.getName(), parameter.getAttributes());
                // Search for any variable defined in the parameters
                for (String attribute : parameter.getAttributes().keySet()) {
                    //#APPOINTMENT%TEMPLATE%<<TEMPLATE_NAME>>|DURATION_TIME/3?ffffff!000000#
                    if (attribute.contains(DURATION_TIME_OPERATION)) {
                        final String[] conditions = attribute.split(Pattern.quote(ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION));
                        if (conditions.length == 2) {
                            //<TEMPLATE_NAME>>
                            final AppointmentDTO appointment = appointmentProvider.getAppointment(userProvider.getUserUUID(droolsSubmittedForm),
                                    conditions[TEMPLATE_NAME_POSITION]);
                            if (appointment != null) {
                                //DURATION_TIME/3?ffffff!000000#
                                final int questionMark = conditions[1].indexOf('?');
                                parameter.getAttributes().put(attribute, getTimeBasedAction(conditions[1].substring(0, questionMark),
                                        conditions[1].substring(questionMark + 1), appointment, timezone,
                                        this.dateToCheck != null ? this.dateToCheck : ZonedDateTime.now()));
                            }
                        }
                        //#APPOINTMENT%TEMPLATE%<<TEMPLATE_NAME>>|STARTING_TIME#
                    } else if (attribute.contains(APPOINTMENT_STARTING_TIME_HOUR)) {
                        final String[] actions = attribute.split(Pattern.quote(ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION));
                        final AppointmentDTO appointment = appointmentProvider.getAppointment(userProvider.getUserUUID(droolsSubmittedForm),
                                actions[TEMPLATE_NAME_POSITION]);
                        if (appointment != null) {
                            parameter.getAttributes().put(attribute, formatTime(appointment.getStartTime(), timezone));
                        }
                        //#APPOINTMENT%TEMPLATE%<<TEMPLATE_NAME>>|ENDING_TIME#
                    } else if (attribute.contains(APPOINTMENT_ENDING_TIME_HOUR)) {
                        final String[] actions = attribute.split(Pattern.quote(ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION));
                        final AppointmentDTO appointment = appointmentProvider.getAppointment(userProvider.getUserUUID(droolsSubmittedForm),
                                actions[TEMPLATE_NAME_POSITION]);
                        if (appointment != null) {
                            parameter.getAttributes().put(attribute, formatTime(appointment.getEndTime(), timezone));
                        }
                    }
                }
            }
        }
    }


    private String getTimeBasedAction(String condition, String action, AppointmentDTO appointment, String timezone, ZonedDateTime dateToCheck) {
        if (appointment == null) {
            return null;
        }

        // condition: DURATION_TIME/3
        // action: ffffff:000000#
        final int separatorIndex = action.indexOf(ValueCalculator.VALUE_SEPARATION);
        if (separatorIndex < 0) {
            return action;
        }

        final String actionSuccess = action.substring(0, separatorIndex);
        final String actionFailure = action.substring(separatorIndex + 1);

        final ZonedDateTime startTime = applyTimezone(appointment.getStartTime(), timezone);
        if (dateToCheck.isBefore(startTime)) {
            return actionFailure;
        }
        if (condition.startsWith(DURATION_TIME_OPERATION)) {
            final Duration duration = Duration.between(appointment.getStartTime(), appointment.getEndTime());

            //Get operator.
            final String operation = condition.substring(DURATION_TIME_OPERATION.length());

            try {
                final ZonedDateTime limit = startTime.plusSeconds((long) valueCalculator
                        .operationExecution(duration.getSeconds() + operation));
                if (dateToCheck.isAfter(limit)) {
                    return actionSuccess;
                }
            } catch (Exception e) {
                InfographicEngineLogger.severe(this.getClass(), "Operation '" + duration.getSeconds() + operation
                        + "' cannot be executed");
            }
        }

        return actionFailure;
    }


    private String formatTime(LocalDateTime localDateTime, String timezone) {
        return applyTimezone(localDateTime, timezone).format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }


    private ZonedDateTime applyTimezone(LocalDateTime localDateTime, String timezone) {
        if (timezone != null && !timezone.isBlank()) {
            try {
                InfographicEngineLogger.debug(this.getClass(), "Converting timezone from '{}' ({}) to '{}' ({}).",
                        localDateTime, ZoneOffset.UTC, localDateTime, ZoneId.of(timezone));
                return localDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of(timezone));
            } catch (DateTimeException e) {
                InfographicEngineLogger.severe(this.getClass(), "Invalid timezone provided '" + timezone + "'.");
                InfographicEngineLogger.errorMessage(this.getClass(), e);
            }
        } else {
            InfographicEngineLogger.warning(this.getClass(), "No timezone provided.");
        }
        return localDateTime.atZone(ZoneId.systemDefault());
    }

    /**
     * Only for testing purposes. Emulate the current time.
     *
     * @param dateToCheck zoned time to check.
     */
    public void setDateToCheck(ZonedDateTime dateToCheck) {
        this.dateToCheck = dateToCheck;
    }
}
