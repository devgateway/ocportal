package org.devgateway.ocds.web.spring;

import org.devgateway.toolkit.persistence.dao.Person;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;

public interface SendEmailService {
    void send(MimeMessagePreparator mimeMessagePreparator) throws MailException;

    void send(SimpleMailMessage simpleMessage) throws MailException;

    void sendEmailResetPassword(Person person, String newPassword);

    void sendNewAccountNotification(Person person, String plainPassword);

    void sendEmail(String subject, String text, String to);
}
