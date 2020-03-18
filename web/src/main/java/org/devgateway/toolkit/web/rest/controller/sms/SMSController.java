package org.devgateway.toolkit.web.rest.controller.sms;

import org.devgateway.toolkit.persistence.dao.sms.SMSMessageWrapper;
import org.devgateway.toolkit.persistence.service.sms.SMSMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SMSController {

    @Autowired
    private SMSMessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(SMSController.class);

    @RequestMapping(value = "/api/smsGateway/yz7jAZSR2d32kpElmrDopbd4mWAjF7IA",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public String smsGateway(@RequestBody SMSMessageWrapper body) {
        String log = "Sms Gateway Received " + body.getMessageCount() + " messages.";
        body.getResults().forEach(m -> messageService.save(m));
        logger.info(log.toString());
        return log;
    }
}
