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
    public String smsGateway(@RequestParam(value = "key", required = false)
                                     String key, @RequestBody Map<String, Object> payload) {
        String log = "Sms Gateway Received for key=" + (key == null ? "[null]" : key) + " payload=" + payload;
        logger.info(log);
        return log;
    }
}
