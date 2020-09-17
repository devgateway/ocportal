package org.devgateway.toolkit.web.spring;

import org.apache.commons.lang3.RandomStringUtils;
import org.devgateway.ocds.web.spring.SendEmailService;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class PasswordRecoveryService {

    public static final int RANDOM_PASSWORD_LENGTH = 16;

    private final PersonService personService;

    private final SendEmailService sendEmailService;

    private final PasswordEncoder passwordEncoder;

    public PasswordRecoveryService(
            PersonService personService,
            SendEmailService sendEmailService,
            PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.sendEmailService = sendEmailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void resetPassword(String email) {
        Person person = personService.findByEmail(email);

        if (person != null) {
            final String newPassword = RandomStringUtils.random(RANDOM_PASSWORD_LENGTH, true, true);
            person.setPassword(passwordEncoder.encode(newPassword));
            person.setChangePasswordNextSignIn(true);

            personService.saveAndFlush(person);
            sendEmailService.sendEmailResetPassword(person, newPassword);
        }
    }
}
