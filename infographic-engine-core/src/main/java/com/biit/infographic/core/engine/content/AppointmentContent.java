package com.biit.infographic.core.engine.content;

import com.biit.appointment.core.models.AppointmentDTO;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.providers.AppointmentProvider;
import com.biit.infographic.core.providers.UserProvider;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
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
    public static final String ATTRIBUTE_FIELDS_SEPARATION = "|";
    public static final String CONDITION_SEPARATION = "?";

    private static final int TEMPLATE_NAME_POSITION = 0;
    private static final int TEMPLATE_CONDITION_POSITION = 1;
    private static final int TEMPLATE_ACTION_POSITION = 2;
    private static final int ACTION_SUCCESS = 0;
    private static final int ACTION_FAILURE = 1;

    private final AppointmentProvider appointmentProvider;
    private final UserProvider userProvider;
    private LocalDateTime dateToCheck;

    public AppointmentContent(AppointmentProvider appointmentProvider,
                              UserProvider userProvider) {
        this.appointmentProvider = appointmentProvider;
        this.userProvider = userProvider;
        this.dateToCheck = LocalDateTime.now();
    }

    public void setAppointmentValues(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm)
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
                    //#APPOINTMENT%TEMPLATE%<<TEMPLATE_NAME>>|DURATION_TIME/3|ffffff:000000#
                    if (attribute.contains(DURATION_TIME_OPERATION)) {
                        final String[] actions = attribute.split(Pattern.quote(ATTRIBUTE_FIELDS_SEPARATION));
                        if (actions.length == TEMPLATE_ACTION_POSITION + 1) {
                            final AppointmentDTO appointment = appointmentProvider.getAppointment(userProvider.getUserUUID(droolsSubmittedForm),
                                    actions[TEMPLATE_NAME_POSITION]);
                            if (appointment != null) {
                                parameter.getAttributes().put(attribute, getTimeBasedAction(actions[TEMPLATE_CONDITION_POSITION],
                                        actions[TEMPLATE_ACTION_POSITION], appointment));
                            }
                        }
                        //#APPOINTMENT%TEMPLATE%<<TEMPLATE_NAME>>|STARTING_TIME#
                    } else if (attribute.contains(APPOINTMENT_STARTING_TIME_HOUR)) {
                        final String[] actions = attribute.split(Pattern.quote(ATTRIBUTE_FIELDS_SEPARATION));
                        final AppointmentDTO appointment = appointmentProvider.getAppointment(userProvider.getUserUUID(droolsSubmittedForm),
                                actions[TEMPLATE_NAME_POSITION]);
                        if (appointment != null) {
                            parameter.getAttributes().put(attribute, appointment.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                        }
                        //#APPOINTMENT%TEMPLATE%<<TEMPLATE_NAME>>|ENDING_TIME#
                    } else if (attribute.contains(APPOINTMENT_ENDING_TIME_HOUR)) {
                        final String[] actions = attribute.split(Pattern.quote(ATTRIBUTE_FIELDS_SEPARATION));
                        final AppointmentDTO appointment = appointmentProvider.getAppointment(userProvider.getUserUUID(droolsSubmittedForm),
                                actions[TEMPLATE_NAME_POSITION]);
                        if (appointment != null) {
                            parameter.getAttributes().put(attribute, appointment.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                        }
                    }
                }
            }
        }
    }


    private String getTimeBasedAction(String condition, String action, AppointmentDTO appointment) {
        if (appointment == null) {
            return null;
        }

        final String[] actions = action.split(Pattern.quote(CONDITION_SEPARATION));
        if (actions.length == 1) {
            return action;
        }

        if (dateToCheck.isBefore(appointment.getStartTime())) {
            return actions[ACTION_FAILURE];
        }
        if (condition.startsWith(DURATION_TIME_OPERATION)) {
            final Duration duration = Duration.between(appointment.getStartTime(), appointment.getEndTime());

            //Get operator.
            final String operation = condition.substring(DURATION_TIME_OPERATION.length());

            try {
                final LocalDateTime limit = appointment.getStartTime().plusSeconds((long) operationExecution(duration.getSeconds() + operation));
                if (dateToCheck.isBefore(limit)) {
                    return actions[ACTION_SUCCESS];
                }
            } catch (Exception e) {
                InfographicEngineLogger.severe(this.getClass(), "Operation '" + duration.getSeconds() + operation
                        + "' cannot be executed");
            }
        }

        return actions[ACTION_FAILURE];
    }

    private double operationExecution(String operation) {
        //7200/10
        if (operation.contains("/")) {
            return generateFraction(operation);
        }
        if (operation.contains("*")) {
            return generateMultiplication(operation);
        }
        try {
            return Double.parseDouble(operation);
        } catch (Exception e) {
            throw new InvalidParameterException(this.getClass(), "Operation '" + operation + "' is invalid!");
        }
    }


    private double generateFraction(String operation) {
        final String[] parts = operation.split("/");
        if (parts.length == 1) {
            return Double.parseDouble(parts[0]);
        }
        return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
    }

    private double generateMultiplication(String operation) {
        final String[] parts = operation.split("\\*");
        if (parts.length == 1) {
            return Long.parseLong(parts[0]);
        }
        return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
    }

    public LocalDateTime getDateToCheck() {
        return dateToCheck;
    }

    public void setDateToCheck(LocalDateTime dateToCheck) {
        this.dateToCheck = dateToCheck;
    }
}
