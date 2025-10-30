package com.biit.infographic.core.providers;

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
import com.biit.appointment.core.models.IAppointmentCenterRestClient;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.utils.pool.BasePool;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppointmentProvider extends BasePool<String, AppointmentDTO> {

    private static final Long APPOINTMENT_POOL_TIME = (long) (5 * 60 * 1000);

    private final IAppointmentCenterRestClient appointmentCenterRestClient;

    public AppointmentProvider(IAppointmentCenterRestClient appointmentCenterRestClient) {
        this.appointmentCenterRestClient = appointmentCenterRestClient;
    }

    public AppointmentDTO getAppointment(UUID userUUID, String appointmentTemplateName) {
        if (userUUID != null && appointmentTemplateName != null) {
            AppointmentDTO appointment = getElement(userUUID + "_" + appointmentTemplateName);
            if (appointment == null) {
                appointment = appointmentCenterRestClient.findByAttendeeAndTemplateCurrent(userUUID, appointmentTemplateName).orElse(null);
                if (appointment == null) {
                    InfographicEngineLogger.severe(this.getClass(), "No appointment found for user '{}' and template '{}'.", userUUID, appointmentTemplateName);
                    throw new ElementDoesNotExistsException(this.getClass(), "\"No appointment found for user '"
                            + userUUID + "' and template '" + appointmentTemplateName + "'.");
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
