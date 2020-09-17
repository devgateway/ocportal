package org.devgateway.toolkit.web.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.web.spring.PasswordRecoveryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Octavian Ciubotaru
 */
@RestController
public class UserController {

    private final PasswordRecoveryService passwordRecoveryService;

    public UserController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping(value = "/api/user/forgotPassword", consumes = APPLICATION_JSON_UTF8_VALUE)
    public void greeting(@RequestBody final JsonNode forgotPasswordRequest) {
        String email = forgotPasswordRequest.get("email").asText();
        if (StringUtils.isNotEmpty(email)) {
            passwordRecoveryService.resetPassword(email);
        }
    }
}
