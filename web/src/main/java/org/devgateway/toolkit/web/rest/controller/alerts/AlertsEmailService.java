package org.devgateway.toolkit.web.rest.controller.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

/**
 * @author idobre
 * @since 23/08/2019
 * <p>
 * Service used to send user email alerts.
 */
@Service
public class AlertsEmailService {
    private static final Logger logger = LoggerFactory.getLogger(AlertsEmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HttpServletRequest request;

    /**
     * Send a secret url that allows user to verify their email address.
     */
    public void sendVerifyEmail(final Alert alert) throws MailException {
        final String url = URI.create(WebSecurityUtil.createURL(request, "/verifyEmail/"))
                .resolve(alert.getSecret()).toASCIIString();

        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage);

            final String content = "Hello,\n\n"
                    + "Before you can receive Makueni Alerts we need to validate your email address.\n"
                    + "You can do this by simply clicking on the link below: \n\n"
                    + "Verify email url: <a style=\"color: #3060ED; text-decoration: none;\" href=\""
                    + url + "\">" + url + "</a>\n\n"
                    + "If you have problems, please paste the above URL into your browser.\n\n"
                    + "Thanks,\n"
                    + "Makueni Portal Team";

            msg.setTo(alert.getEmail());
            msg.setFrom("noreply@dgstg.org");
            msg.setSubject("Makueni OC Portal - Please Verify Email Address");
            msg.setText(content.replaceAll("\n", "<br />"), true);
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error("Failed to send verification email for: " + alert.getEmail(), e);
            throw e;
        }
    }

    public void sendEmailAlert(final Alert alert, final MimeMessagePreparator message) throws MailException {
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            logger.error("Failed to send email alert for for: " + alert.getEmail(), e);
            throw e;
        }
    }


    public static String createURL(final HttpServletRequest request, final String resourcePath) {
        final int port = request.getServerPort();
        final StringBuilder result = new StringBuilder();

        result.append(request.getScheme())
                .append("://")
                .append(request.getServerName());

        if ((request.getScheme().equals("http") && port != 80)
                || (request.getScheme().equals("https") && port != 443)) {
            result.append(':')
                    .append(port);
        }

        result.append(request.getContextPath());

        if (resourcePath != null && resourcePath.length() > 0) {
            if (!resourcePath.startsWith("/")) {
                result.append("/");
            }
            result.append(resourcePath);
        }

        return result.toString();
    }
}
