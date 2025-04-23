package com.biit.infographic.core.email;

import com.biit.logger.mail.SendEmail;
import com.biit.logger.mail.SendEmailThread;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.server.email.EmailSendPool;
import com.biit.server.email.ServerEmailService;
import com.biit.server.logger.EmailServiceLogger;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.client.providers.UserManagerClient;
import com.biit.utils.file.FileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class InfographicEmailService extends ServerEmailService {

    private static final String USER_REPORT_EMAIL_TEMPLATE = "email-templates/parchment.html";
    private static final String USER_REPORT_READY_EMAIL_TEMPLATE = "email-templates/parchment.html";
    private static final String USER_SUPERVISOR_EMAIL_TEMPLATE = "email-templates/parchment.html";

    @Value("#{new Boolean('${mail.server.enabled:false}')}")
    private boolean mailEnabled;

    @Value("${mail.server.smtp.server:#{null}}")
    private String smtpServer;

    @Value("${mail.server.smtp.port:587}")
    private String smtpPort;

    @Value("${mail.server.smtp.username:#{null}}")
    private String emailUser;

    @Value("${mail.server.smtp.password:#{null}}")
    private String emailPassword;

    @Value("${mail.sender:#{null}}")
    private String emailSender;

    @Value("${mail.copy.address:#{null}}")
    private String mailCopy;

    @Value("#{new Boolean('${mail.send.report:false}')}")
    private boolean sendReportByMail;

    @Value("${mail.supervisor.address:#{null}}")
    private String supervisorEmail;

    @Value("#{new Boolean('${mail.report.confirmation.supervisor.enabled:false}')}")
    private boolean mailSupervisorEnabled;

    @Value("#{new Boolean('${mail.report.confirmation.user.enabled:false}')}")
    private boolean mailConfirmationReportToUserEnabled;

    @Value("${mail.dashboard.link:}")
    private String dashboardLink;

    @Value("${infographics.not.sent.by.mail:}")
    private List<String> infographicsIgnoredNames;

    private final MessageSource messageSource;

    private static final Locale LOCALE = Locale.ENGLISH;

    private final EmailConfirmationPool emailConfirmationPool;
    private final EmailSupervisorConfirmationPool emailSupervisorConfirmationPool;

    private final UserManagerClient userManagerClient;

    public InfographicEmailService(Optional<EmailSendPool> emailSendPool,
                                   Optional<EmailConfirmationPool> emailConfirmationPool,
                                   Optional<EmailSupervisorConfirmationPool> emailSupervisorConfirmationPool,
                                   MessageSource messageSource, UserManagerClient userManagerClient) {
        super(emailSendPool, messageSource);
        this.emailConfirmationPool = emailConfirmationPool.orElse(null);
        this.emailSupervisorConfirmationPool = emailSupervisorConfirmationPool.orElse(null);
        this.messageSource = messageSource;
        this.userManagerClient = userManagerClient;
    }


    public void sendPdfInfographic(String mailTo, String submittedBy, String formName, byte[] pdfForm)
            throws EmailNotSentException, InvalidEmailAddressException, FileNotFoundException {
        if (!mailEnabled || !sendReportByMail) {
            EmailServiceLogger.debug(this.getClass(), "Emails disabled. No report pdf email sent.");
            return;
        }
        if (infographicsIgnoredNames.contains(formName)) {
            EmailServiceLogger.warning(this.getClass(), "Infographic '{}' is marked as ignorable. Email will not be sent.", formName);
            return;
        }
        if (mailTo != null) {
            if (smtpServer != null && emailUser != null) {
                final String[] args = new String[]{submittedBy};
                final Locale userLocale = getUserLocale(userManagerClient.findByEmailAddress(mailTo).orElse(null));
                EmailServiceLogger.info(this.getClass(), "Sending form '{}' to email '{}' by '{}'.", formName, mailTo, submittedBy);
                final String emailTemplate = populateUserPdfMailFields(FileReader.getResource(USER_REPORT_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                        args, userLocale);
                sendTemplate(mailTo, getMessage("pdf.infographic.mail.subject", null, userLocale),
                        emailTemplate, getMessage("pdf.infographic.mail.text", args, userLocale), pdfForm, formName + ".pdf");
            } else {
                EmailServiceLogger.debug(this.getClass(), "Email settings not set. Emails will be ignored.");
                EmailServiceLogger.debug(this.getClass(), "Values are smtpServer '{}', emailUser '{}'.",
                        smtpServer, emailUser);
                throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
            }
        } else {
            EmailServiceLogger.warning(this.getClass(), "No emailTo property set. Ignoring emails.");
        }
    }


    public void sendUserHasAReportEmail(String email)
            throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (!mailEnabled || !mailConfirmationReportToUserEnabled) {
            EmailServiceLogger.debug(this.getClass(), "Emails disabled. No report confirmation email sent.");
            return;
        }
        if (email != null && (emailConfirmationPool == null || (emailConfirmationPool.getElement(email) == null))
                && (smtpServer != null && emailUser != null)) {
            final String[] args = new String[]{email};
            final Locale userLocale = getUserLocale(userManagerClient.findByEmailAddress(email).orElse(null));
            final String emailTemplate = populateUserHasAReportFields(FileReader.getResource(USER_REPORT_READY_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    args, userLocale);
            sendTemplate(email, getMessage("user.infographic.mail.subject", args, userLocale),
                    emailTemplate, getMessage("user.infographic.mail.text", args, userLocale));
        }
        if (emailConfirmationPool != null) {
            emailConfirmationPool.addElement(email, email);
        }
    }


    public void sendUserHasAReportToManagerEmail(String userEmail, String name, String lastname)
            throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (!mailEnabled || !mailSupervisorEnabled) {
            EmailServiceLogger.debug(this.getClass(), "Emails disabled. No supervisor confirmation email sent.");
            return;
        }
        //Avoid testing emails if possible.
        if (SendEmailThread.filterMails(userEmail).isEmpty()) {
            EmailServiceLogger.debug(this.getClass(), "Email is from a testing domain. Ignoring email to manager.");
            return;
        }
        if (supervisorEmail != null && userEmail != null && (emailSupervisorConfirmationPool == null
                || (emailSupervisorConfirmationPool.getElement(userEmail) == null)) && (smtpServer != null && emailUser != null)) {
            final Locale userLocale = getUserLocale(userManagerClient.findByEmailAddress(userEmail).orElse(null));
            final String[] args = new String[]{userEmail, name, lastname, dashboardLink};
            final String emailTemplate = populateUserHasAReportToManagerFields(
                    FileReader.getResource(USER_SUPERVISOR_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    args, userLocale);
            sendTemplate(supervisorEmail, getMessage("supervisor.infographic.mail.subject", args, userLocale),
                    emailTemplate, getMessage("supervisor.infographic.mail.text", args, userLocale));
        }
        if (emailSupervisorConfirmationPool != null) {
            emailSupervisorConfirmationPool.addElement(userEmail, userEmail);
        }
    }


    private void sendTemplate(String email, String mailSubject, String emailTemplate, String plainText, byte[] pdfForm, String attachmentName)
            throws EmailNotSentException, InvalidEmailAddressException {
        if (!mailEnabled) {
            return;
        }
        if (smtpServer != null && emailUser != null) {
            SendEmail.sendEmail(smtpServer, smtpPort, emailUser, emailPassword, emailSender, Collections.singletonList(email), null,
                    mailCopy != null ? Collections.singletonList(mailCopy) : null, mailSubject,
                    emailTemplate, plainText, pdfForm, MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE, attachmentName);
            EmailServiceLogger.info(this.getClass(), "Email sent!");
        } else {
            EmailServiceLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            EmailServiceLogger.debug(this.getClass(), "Values are smtpServer '{}', emailUser '{}'.",
                    smtpServer, emailUser);
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }


    private String populateUserPdfMailFields(String html, Object[] args, Locale locale) {
        return html.replace(EMAIL_TITLE_TAG, getMessage("pdf.infographic.mail.title", args, locale))
                .replace(EMAIL_SUBTITLE_TAG, getMessage("pdf.infographic.mail.subtitle", args, locale))
                .replace(EMAIL_BODY_TAG, getMessage("pdf.infographic.mail.body", args, locale))
                .replace(EMAIL_FOOTER_TAG, getMessage("pdf.infographic.mail.footer", args, locale));
    }


    private String populateUserHasAReportFields(String html, Object[] args, Locale locale) {
        return html.replace(EMAIL_TITLE_TAG, getMessage("user.infographic.mail.title", args, locale))
                .replace(EMAIL_SUBTITLE_TAG, getMessage("user.infographic.mail.subtitle", args, locale))
                .replace(EMAIL_BODY_TAG, getMessage("user.infographic.mail.body", args, locale))
                .replace(EMAIL_FOOTER_TAG, getMessage("user.infographic.mail.footer", args, locale));
    }


    private String populateUserHasAReportToManagerFields(String html, Object[] args, Locale locale) {
        return html.replace(EMAIL_TITLE_TAG, getMessage("supervisor.infographic.mail.title", args, locale))
                .replace(EMAIL_SUBTITLE_TAG, getMessage("supervisor.infographic.mail.subtitle", args, locale))
                .replace(EMAIL_BODY_TAG, getMessage("supervisor.infographic.mail.body", args, locale))
                .replace(EMAIL_FOOTER_TAG, getMessage("supervisor.infographic.mail.footer", args, locale));
    }

    /**
     * Must be duplicated this method to ensure message source loads the correct context?
     *
     * @return
     */
    @Override
    protected String getMessage(String key, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            EmailServiceLogger.severe(this.getClass(), e.getMessage());
            return key;
        }
    }

    private Locale getUserLocale(IAuthenticatedUser user) {
        if (user != null && user.getLocale() != null) {
            return user.getLocale();
        }
        return LOCALE;
    }
}
