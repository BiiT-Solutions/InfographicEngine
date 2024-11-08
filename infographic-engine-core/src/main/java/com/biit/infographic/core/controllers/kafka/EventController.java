package com.biit.infographic.core.controllers.kafka;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.controllers.kafka.converter.EventConverter;
import com.biit.infographic.core.email.InfographicEmailService;
import com.biit.infographic.core.exceptions.MalformedTemplateException;
import com.biit.infographic.core.pdf.PdfController;
import com.biit.infographic.logger.EventsLogger;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.kafka.consumers.EventListener;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.client.providers.UserManagerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private final DroolsResultController droolsResultController;

    private final InfographicEventSender infographicEventSender;

    private final PdfController pdfController;

    private final InfographicEmailService infographicEmailService;

    private final UserManagerClient userManagerClient;

    private final String smtpServer;

    private final InfographicPdfEventSender infographicPdfEventSender;

    private EventController() {
        this.eventConverter = null;
        this.droolsResultRepository = null;
        this.droolsResultController = null;
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
                           DroolsResultController droolsResultController, InfographicEventSender infographicEventSender,
                           PdfController pdfController, InfographicEmailService infographicEmailService,
                           UserManagerClient userManagerClient, @Value("${mail.server.smtp.server:#{null}}") String smtpServer,
                           InfographicPdfEventSender infographicPdfEventSender) {
        this.eventConverter = eventConverter;
        this.droolsResultRepository = droolsResultRepository;
        this.droolsResultController = droolsResultController;
        this.infographicEventSender = infographicEventSender;
        this.pdfController = pdfController;
        this.infographicEmailService = infographicEmailService;
        this.userManagerClient = userManagerClient;
        this.smtpServer = smtpServer;
        this.infographicPdfEventSender = infographicPdfEventSender;

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
            if (event.getCustomProperties() != null) {
                if (!Objects.equals(event.getCustomProperty(EventCustomProperties.FACT_TYPE), ALLOWED_FACT_TYPE)) {
                    EventsLogger.debug(this.getClass(), "Event ignored.");
                    return;
                }
            }
            final DroolsSubmittedForm droolsForm = getDroolsForm(event);
            if (droolsForm != null) {
                EventsLogger.info(this.getClass(), "Received Drools Result '{}'/'{}'.", droolsForm.getName(), event.getTag());
                droolsResultRepository.save(eventConverter.getDroolsContent(event, droolsForm));
                EventsLogger.debug(this.getClass(), "Drools Result '{}'/'{}' saved.", droolsForm.getName(), event.getTag());
                //As Drools now can execute multiples rules from one form, the rule form name is on the event tag.
                final GeneratedInfographic generatedInfographic = droolsResultController.process(droolsForm, event.getTag(), createdBy,
                        event.getOrganization(), null);
                infographicEventSender.sendResultEvents(generatedInfographic, createdBy, event.getOrganization(), event.getSessionId());

                final byte[] pdfForm = pdfController.generatePdfFromSvgs(generatedInfographic.getSvgContents());
                //Send PDF by email
                sendInfographicByMail(generatedInfographic, pdfForm, createdBy);
                //Send PDF as event
                sendInfographicAsEvent(generatedInfographic, pdfForm, event.getSessionId(), event.getOrganization());

            } else {
                EventsLogger.debug(this.getClass(), "No drools form obtained.");
            }
        } catch (MalformedTemplateException e) {
            EventsLogger.warning(this.getClass(), "Template does not exists!. " + e.getMessage());
        } catch (JsonProcessingException e) {
            EventsLogger.severe(this.getClass(), "Event cannot be parsed!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        } catch (Exception e) {
            EventsLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

    private DroolsSubmittedForm getDroolsForm(Event event) throws JsonProcessingException {
        return event.getEntity(DroolsSubmittedForm.class);
    }


    private void sendInfographicByMail(GeneratedInfographic generatedInfographic, byte[] pdfData, String createdBy) {
        try {
            //Check if email is enabled now to avoid the search for a user.
            if (smtpServer != null) {
                final Optional<IAuthenticatedUser> user = userManagerClient.findByUsername(createdBy);
                if (user.isPresent()) {
                    infographicEmailService.sendPdfInfographic(user.get().getEmailAddress(), generatedInfographic.getCreatedBy(),
                            generatedInfographic.getFormName(), pdfData);
                }
            }
        } catch (Exception e) {
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

    private void sendInfographicAsEvent(GeneratedInfographic generatedInfographic, byte[] pdfData, UUID sessionId, String organization) {
        infographicPdfEventSender.sendPdfInfographic(generatedInfographic, pdfData, sessionId, organization);
    }
}
