package com.biit.infographic.core.controllers.kafka;

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
import com.biit.infographic.core.controllers.kafka.converter.EventConverter;
import com.biit.infographic.core.email.InfographicEmailService;
import com.biit.infographic.core.exceptions.MalformedTemplateException;
import com.biit.infographic.core.pdf.PdfController;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.logger.EventsLogger;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.kafka.consumers.EventListener;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.usermanager.client.providers.UserManagerClient;
import com.biit.usermanager.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;


@Controller
@ConditionalOnExpression("${spring.kafka.enabled:false}")
public class EventController {
    public static final String ALLOWED_FACT_TYPE = "DroolsResultForm";

    private final EventConverter eventConverter;

    private final DroolsResultRepository droolsResultRepository;

    private final InfographicEventSender infographicEventSender;

    private final PdfController pdfController;

    private final InfographicEmailService infographicEmailService;

    private final UserManagerClient userManagerClient;

    private final String smtpServer;

    private final InfographicPdfEventSender infographicPdfEventSender;

    private final GeneratedInfographicProvider generatedInfographicProvider;

    private EventController() {
        this.generatedInfographicProvider = null;
        this.eventConverter = null;
        this.droolsResultRepository = null;
        this.infographicEventSender = null;
        this.pdfController = null;
        this.infographicEmailService = null;
        this.userManagerClient = null;
        this.smtpServer = null;
        this.infographicPdfEventSender = null;
    }

    @Autowired(required = false)
    public EventController(EventListener eventListener,
                           EventConverter eventConverter,
                           DroolsResultRepository droolsResultRepository,
                           InfographicEventSender infographicEventSender,
                           PdfController pdfController, InfographicEmailService infographicEmailService,
                           UserManagerClient userManagerClient, @Value("${mail.server.smtp.server:#{null}}") String smtpServer,
                           InfographicPdfEventSender infographicPdfEventSender, GeneratedInfographicProvider generatedInfographicProvider) {
        this.eventConverter = eventConverter;
        this.droolsResultRepository = droolsResultRepository;
        this.infographicEventSender = infographicEventSender;
        this.pdfController = pdfController;
        this.infographicEmailService = infographicEmailService;
        this.userManagerClient = userManagerClient;
        this.smtpServer = smtpServer;
        this.infographicPdfEventSender = infographicPdfEventSender;
        this.generatedInfographicProvider = generatedInfographicProvider;

        //Listen to a topic
        if (eventListener != null) {
            eventListener.addListener((event, offset, groupId, key, partition, topic, timeStamp) ->
                    eventHandler(event, groupId, key, partition, topic, timeStamp));
        }
    }

    public void eventHandler(Event event, String groupId, String key, int partition, String topic, long timeStamp) {
        EventsLogger.debug(this.getClass(), "Received event '{}' on topic '{}', group '{}', key '{}', partition '{}' at '{}'",
                event, topic, groupId, key, partition, LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                        TimeZone.getDefault().toZoneId()));

        final String createdBy = event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag()) != null
                ? event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag())
                : event.getCreatedBy();

        try {
            if (event.getCustomProperties() != null && !Objects.equals(event.getCustomProperty(EventCustomProperties.FACT_TYPE), ALLOWED_FACT_TYPE)) {
                EventsLogger.debug(this.getClass(), "Event ignored.");
                return;
            }
            final DroolsSubmittedForm droolsForm = getDroolsForm(event);
            if (droolsForm != null) {
                EventsLogger.info(this.getClass(), "Received Drools Result '{}'/'{}' v{} from '{}'.", droolsForm.getName(), event.getTag(),
                        droolsForm.getVersion(), event.getCreatedBy());
                droolsResultRepository.save(eventConverter.getDroolsContent(event, droolsForm));
                EventsLogger.debug(this.getClass(), "Drools Result '{}'/'{}' v{} saved.", droolsForm.getName(), event.getTag(), droolsForm.getVersion());
                //As Drools now can execute multiples rules from one form, the rule form name is on the event tag.
                final Optional<GeneratedInfographic> generatedInfographic = generatedInfographicProvider.process(droolsForm, event.getTag(), createdBy,
                        event.getOrganization(), event.getUnit(), null, null);
                if (generatedInfographic.isPresent()) {
                    infographicEventSender.sendResultEvents(generatedInfographic.get(), createdBy, event.getOrganization(),
                            event.getSessionId(), event.getUnit());

                    final byte[] pdfForm = pdfController.generatePdfFromSvgs(generatedInfographic.get().getSvgContents());
                    //Send PDF by email
                    sendInfographicByMail(generatedInfographic.get(), pdfForm, createdBy);
                    //Send PDF as event
                    sendInfographicAsEvent(generatedInfographic.get(), pdfForm, event.getSessionId(), event.getOrganization(), event.getUnit());
                } else {
                    EventsLogger.warning(this.getClass(), "No infographic generated!");
                }
            } else {
                EventsLogger.debug(this.getClass(), "No drools form obtained.");
            }
        } catch (MalformedTemplateException e) {
            EventsLogger.warning(this.getClass(), "Template does not exists!. " + e.getMessage());
        } catch (Exception e) {
            EventsLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

    private DroolsSubmittedForm getDroolsForm(Event event) {
        return event.getEntity(DroolsSubmittedForm.class);
    }


    private void sendInfographicByMail(GeneratedInfographic generatedInfographic, byte[] pdfData, String createdBy) {
        try {
            //Check if email is enabled now to avoid the search for a user.
            if (smtpServer != null) {
                final Optional<UserDTO> user = userManagerClient.findByUsername(createdBy);
                if (user.isPresent()) {
                    try {
                        infographicEmailService.sendPdfInfographic(user.get().getEmailAddress(), createdBy,
                                generatedInfographic.getFormName(), pdfData);
                    } catch (Exception e) {
                        EventsLogger.errorMessage(this.getClass(), e);
                    }
                    try {
                        infographicEmailService.sendUserHasAReportEmail(user.get().getEmailAddress());
                    } catch (Exception e) {
                        EventsLogger.errorMessage(this.getClass(), e);
                    }
                    try {
                        infographicEmailService.sendUserHasAReportToManagerEmail(user.get().getEmailAddress(),
                                user.get().getName(), user.get().getLastname());
                    } catch (Exception e) {
                        EventsLogger.errorMessage(this.getClass(), e);
                    }
                }
            }
        } catch (Exception e) {
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

    private void sendInfographicAsEvent(GeneratedInfographic generatedInfographic, byte[] pdfData, UUID sessionId,
                                        String organization, String unit) {
        infographicPdfEventSender.sendPdfInfographic(generatedInfographic, pdfData, sessionId, organization, unit);
    }
}
