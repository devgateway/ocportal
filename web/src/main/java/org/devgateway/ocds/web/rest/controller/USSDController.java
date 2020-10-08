package org.devgateway.ocds.web.rest.controller;

import org.devgateway.ocds.web.spring.USSDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Integration with Onfon Media USSD service.
 *
 * @author Octavian Ciubotaru
 */
@RestController
public class USSDController {

    @Autowired
    private USSDService ussdService;

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

    @PostMapping(value = "/api/alerts/ussd", produces = "application/json")
    public String processUSSDMessage(@RequestBody USSDMessage message) {
        return ussdService.process(message);
    }
}
