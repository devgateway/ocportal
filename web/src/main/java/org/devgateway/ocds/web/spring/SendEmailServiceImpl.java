/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocds.web.spring;

import com.google.common.net.InternetDomainName;
import org.apache.commons.mail.util.MimeMessageParser;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.web.rest.controller.alerts.AlertsEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import static org.devgateway.toolkit.persistence.dao.DBConstants.INSTANCE_NAME;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_USER;

/**
 * Service to send emails to users to validate email addresses or reset passwords
 *
 * @author mpostelnicu
 */
@Service
public class SendEmailServiceImpl implements SendEmailService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HttpServletRequest request;

    private String fromEmail;

    @Value("${serverURL}")
    private String serverURL;

    @Value("${disableEmailSending:#{false}}")
    private Boolean disableEmailSending;

    @PostConstruct
    public void postConstruct() {
        if (disableEmailSending) {
            logger.warn("**** SENDING OF EMAILS IS DISABLED !! ****");
        }
        createFromEmail();
    }

    @Override
    public String getFromEmail() {
        return fromEmail;
    }

    public void createFromEmail() {
        fromEmail = "noreply@" + getDomainName(serverURL);
    }

    /**
     * Gets domain name. Returns to toplevel domain if the server runs on dgstg.org, returns full domain otherwise
     *
     * @param url
     * @return
     */
    public static String getDomainName(final String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String domain = uri.getHost();
        if (domain.equals("localhost")) {
            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        InternetDomainName fromDomain = InternetDomainName.from(domain);
        return domain.contains("dgstg.org") ? fromDomain.topPrivateDomain().toString() : fromDomain.toString();
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        if (disableEmailSending) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            try {
                mimeMessagePreparator.prepare(mimeMessage);
                MimeMessageParser parser = new MimeMessageParser(mimeMessage);
                parser.parse();
                if (parser.hasPlainContent()) {
                    logger.info("Prepared Plain Message: " + parser.getPlainContent());
                }
                if (parser.hasHtmlContent()) {
                    logger.info("Prepared Html Message: " + parser.getHtmlContent());
                }

            } catch (Exception e) {
                throw new MailPreparationException(e);
            }
        } else {
            javaMailSender.send(mimeMessagePreparator);
        }

    }


    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        if (disableEmailSending) {
            logger.info("Prepared Simple Message: " + simpleMessage.toString());
        } else {
            javaMailSender.send(simpleMessage);
        }
    }

    /**
     * Send a reset password email. This is UNSAFE because passwords are sent in clear text.
     * Nevertheless some customers will ask for these emails to be sent, so ...
     *
     * @param person
     * @param newPassword
     */
    @Override
    public void sendEmailResetPassword(final Person person, final String newPassword) {
        final SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(person.getEmail());
        msg.setFrom(getFromEmail());
        msg.setSubject("Recover your password");
        msg.setText("Dear " + person.getFirstName() + " " + person.getLastName() + ",\n\n"
                + "These are your new login credentials for Nandi.\n\n"
                + "Username: " + person.getUsername() + "\n"
                + "Password: " + newPassword + "\n\n"
                + "At login, you will be prompted to change your password to one of your choice.\n\n" + "Thank you,\n"
                + "DG Team");
        try {
            javaMailSender.send(msg);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a notification about account creation to its owner.
     *
     * @param person person to be notified
     */
    @Override
    public void sendNewAccountNotification(final Person person, final String plainPassword) {
        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage);

            String url;
            if (person.getRoles().stream().anyMatch(r -> r.getAuthority().equals(ROLE_PMC_USER))) {
                url = "\nPlease install PMC Reporter app from Google Play\n"
                        + "<a href='https://play.google.com/store/apps/details?id=" + DBConstants.ANDROID_PACKAGE_NAME
                        + "'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/"
                        + "images/badges/en_badge_web_generic.png' style='width: 250px;'/>"
                        + "</a>";
            } else {
                url = "URL: " + AlertsEmailService.createURL(request, "");
            }

            final String content = "Dear " + person.getFirstName() + " " + person.getLastName() + ",\n\n"
                    + "Your Username and Password to access the system are as follows:\n"
                    + "Username: " + person.getUsername() + "\n"
                    + "Password: " + plainPassword + "\n"
                    + url + "\n\n"
                    + "For the first time you Login with the password given, you will be required to:\n"
                    + "1. Change your password: Please ensure that your new password is known only to you. "
                    + "Password should have a minimum of 6 characters where 2 of them must be numbers and "
                    + "at least 1 special character.\n"
                    + "2. Subsequently you may change your password on a need basis.\n\n"
                    + "Kind regards,\n"
                    + INSTANCE_NAME + " Team";
            msg.setTo(person.getEmail());
            msg.setFrom(getFromEmail());
            msg.setSubject("Your " + INSTANCE_NAME + " Account");
            msg.setText(content.replaceAll("\n", "<br />"), true);
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error("Failed to send new account email to userId=" + person.getId(), e);
        }
    }

    @Override
    public void sendEmail(final String subject, final String text, final String to) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom("support@developmentgateway.org");
        msg.setSubject(subject);
        msg.setText(text);
        try {
            logger.info("Sending email " + msg);
            send(msg);
        } catch (MailException e) {
            e.printStackTrace();
        }

    }
}
