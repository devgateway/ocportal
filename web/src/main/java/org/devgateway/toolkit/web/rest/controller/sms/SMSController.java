package org.devgateway.toolkit.web.rest.controller.sms;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SMSController {

    @RequestMapping(value = "/api/smsGateway",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public void smsGateway(@RequestParam(value = "key") String key, @RequestBody Map<String, Object> payload) {
        System.out.println("Sms Gateway Received for key=" + key + " payload=" + payload);
    }
}
