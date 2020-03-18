package org.devgateway.toolkit.web.rest.controller.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SMSController {

    private static final Logger logger = LoggerFactory.getLogger(SMSController.class);


    @RequestMapping(value = "/api/smsGateway/yz7jAZSR2d32kpElmrDopbd4mWAjF7IA",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public String smsGateway(@RequestBody Map<String, Object> payload) {
        String log = "Sms Gateway Received payload=" + payload;
        logger.info(log);
        return log;
    }
}
