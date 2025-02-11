package org.devgateway.ocds.web.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.web.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mpostelnicu on 16-May-17.
 */
@RestController
public class AuthenticatedCheckerController {

    public static class AuthenticatedCheckerResponse {
        private Boolean authenticated;
        private Boolean disabledApiSecurity;

        public Boolean getAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(Boolean authenticated) {
            this.authenticated = authenticated;
        }

        public Boolean getDisabledApiSecurity() {
            return disabledApiSecurity;
        }

        public void setDisabledApiSecurity(Boolean disabledApiSecurity) {
            this.disabledApiSecurity = disabledApiSecurity;
        }
    }

    @Autowired
    protected AdminSettingsRepository adminSettingsRepository;

    @Operation(summary = "Returns true if the user is authenticated, false otherwise")
    @RequestMapping(value = "/isAuthenticated", method = {RequestMethod.GET, RequestMethod.POST})
    public AuthenticatedCheckerResponse isAuthenticated(final HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuthenticatedCheckerResponse r = new AuthenticatedCheckerResponse();

        r.setAuthenticated(false);
        r.setDisabledApiSecurity(SecurityUtil.getDisabledApiSecurity(adminSettingsRepository));

        if (authentication != null && authentication.isAuthenticated()) {
            r.setAuthenticated(true);
        }

        return r;
    }

}
