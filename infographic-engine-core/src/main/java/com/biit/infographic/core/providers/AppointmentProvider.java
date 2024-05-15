package com.biit.infographic.core.providers;

import com.biit.appointment.core.models.AppointmentDTO;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.appointment.core.models.IAppointmentCenterRestClient;
import com.biit.utils.pool.BasePool;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppointmentProvider extends BasePool<String, AppointmentDTO> {
    private static final Long APPOINTMENT_POOL_TIME = (long) (5 * 60 * 1000);


    private final IAppointmentCenterRestClient appointmentCenterClient;

    public AppointmentProvider(IAppointmentCenterRestClient appointmentCenterClient) {
        this.appointmentCenterClient = appointmentCenterClient;
    }

    public AppointmentDTO getAppointment(UUID userUUID, String appointmentTemplateName) {
        if (userUUID != null && appointmentTemplateName != null) {
            AppointmentDTO appointment = getElement(userUUID + "_" + appointmentTemplateName);
            if (appointment == null) {
                appointment = appointmentCenterClient.findByAttendeeAndTemplateCurrent(userUUID, appointmentTemplateName).orElse(null);
                if (appointment == null) {
                    InfographicEngineLogger.severe(this.getClass(), "No appointment found for user '{}' and template '{}'.", userUUID, appointmentTemplateName);
                } else {
                    addElement(appointment, userUUID + "_" + appointmentTemplateName);
                }
            }
            return appointment;
        } else {
            InfographicEngineLogger.warning(this.getClass(), "No user or template provided!");
            return null;
        }
    }

    @Override
    public long getExpirationTime() {
        return APPOINTMENT_POOL_TIME;
    }

    @Override
    public boolean isDirty(AppointmentDTO element) {
        return false;
    }
}
