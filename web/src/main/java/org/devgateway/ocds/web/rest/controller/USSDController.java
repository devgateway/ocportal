package org.devgateway.ocds.web.rest.controller;

import org.devgateway.ocds.web.spring.USSDProperties;
import org.devgateway.ocds.web.spring.USSDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Integration with Onfon Media USSD service.
 *
 * @author Octavian Ciubotaru
 */
@RestController
public class USSDController {

    @Autowired
    private USSDService ussdService;

    @Autowired
    private USSDProperties ussdProperties;

    public static class USSDMessage {

        private String msisdn;
        private String sessionId;
        private String serviceCode;
        private String ussdString;

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getUssdString() {
            return ussdString;
        }

        public void setUssdString(String ussdString) {
            this.ussdString = ussdString;
        }
    }

    @PostMapping(value = "/api/alerts/ussd/{secret}", produces = "application/json")
    public String processUSSDMessage(
            @RequestBody USSDMessage message,
            @PathVariable String secret) {

        if (!ussdProperties.getEndpointSecret().equals(secret)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ussdService.process(message);
    }
}
