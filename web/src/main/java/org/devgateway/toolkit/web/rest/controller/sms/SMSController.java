package org.devgateway.toolkit.web.rest.controller.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SMSController {

    private static final Logger logger = LoggerFactory.getLogger(SMSController.class);


    @RequestMapping(value = "/api/smsGateway",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public String smsGateway(@RequestParam(value = "key") String key, @RequestBody Map<String, Object> payload) {
        logger.info("Sms Gateway Received for key=" + key + " payload=" + payload);
        return "Sms Gateway Received for key=" + key + " payload=" + payload;
    }
}
