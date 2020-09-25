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

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_USER;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.web.rest.controller.alerts.AlertsEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Service to send emails to users to validate email addresses or reset passwords
 *
 * @author mpostelnicu
 */
@Component
public class SendEmailService {
    private static Logger logger = LoggerFactory.getLogger(SendEmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HttpServletRequest request;

    /**
     * Send a reset password email. This is UNSAFE because passwords are sent in clear text.
     * Nevertheless some customers will ask for these emails to be sent, so ...
     *
     * @param person
     * @param newPassword
     */
    public void sendEmailResetPassword(final Person person, final String newPassword) {
        final SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(person.getEmail());
        msg.setFrom(DBConstants.FROM_EMAIL);
        msg.setSubject("Recover your password");
        msg.setText("Dear " + person.getFirstName() + " " + person.getLastName() + ",\n\n"
                + "These are your new login credentials for Makueni.\n\n" + "Username: " + person.getUsername() + "\n"
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
    public void sendNewAccountNotification(final Person person, final String plainPassword) {
        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage);

            String url;
            if (person.getRoles().stream().anyMatch(r -> r.getAuthority().equals(ROLE_PMC_USER))) {
                url = "\nPlease install PMC Reporter app from Google Play\n"
                        + "<a href='https://play.google.com/store/apps/details?id=org.devgateway.makueni.pmcdc&hl=en&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'>"
                        + "<img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' style='width: 250px;'/>"
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
                    + "Makueni Team";
            msg.setTo(person.getEmail());
            msg.setFrom(DBConstants.FROM_EMAIL);
            msg.setSubject("Your Makueni Account");
            msg.setText(content.replaceAll("\n", "<br />"), true);
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error("Failed to send new account email to userId=" + person.getId(), e);
        }
    }

    public void sendEmail(final String subject, final String text, final String to) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom("support@developmentgateway.org");
        msg.setSubject(subject);
        msg.setText(text);
        try {
            logger.info("Sending email " + msg);
            javaMailSender.send(msg);
        } catch (MailException e) {
            e.printStackTrace();
        }

    }
}
