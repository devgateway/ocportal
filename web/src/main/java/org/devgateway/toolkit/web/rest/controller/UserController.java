package org.devgateway.toolkit.web.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.web.spring.PasswordRecoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Octavian Ciubotaru
 */
@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final PasswordRecoveryService passwordRecoveryService;

    public UserController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping(value = "/api/user/forgotPassword", consumes = APPLICATION_JSON_UTF8_VALUE)
    public void forgotPassword(@RequestBody final JsonNode forgotPasswordRequest) {
        String email = forgotPasswordRequest.get("email").asText();
        if (StringUtils.isNotEmpty(email)) {
            passwordRecoveryService.resetPassword(email);
        }
    }

    @PostMapping(value = "/api/user/changePassword", consumes = APPLICATION_JSON_UTF8_VALUE)
    public void changePassword(@RequestBody final JsonNode request) {
        try {
            String oldPassword = request.get("oldPassword").asText();
            String newPassword = request.get("newPassword").asText();

            String principalUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            passwordRecoveryService.changePassword(principalUsername, oldPassword, newPassword);
        } catch (Exception e) {
            logger.error("Failed to change the password", e); // error goes just to logs
            throw new RuntimeException("Failed to change the password");
        }
    }
}
