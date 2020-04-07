package org.devgateway.toolkit.web.rest.controller.sms;

import org.devgateway.toolkit.persistence.dao.sms.SMSMessageWrapper;
import org.devgateway.toolkit.persistence.service.sms.SMSMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SMSController {

    @Autowired
    private SMSMessageService messageService;

    @Value("${smsgateway.key}")
    private String smsGatewayKey;

    private static final Logger logger = LoggerFactory.getLogger(SMSController.class);

    @RequestMapping(value = "/api/smsGateway/{key}",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> smsGateway(@RequestBody SMSMessageWrapper body, @PathVariable String key) {
        String log = "Sms Gateway Received " + body.getMessageCount() + " messages.";
        if (!smsGatewayKey.equals(key)) {
            logger.error("Invalid smsGatewayKey provided: " + key);
            return ResponseEntity.badRequest().build();
        }
        body.getResults().forEach(m -> {
            messageService.save(m);
            messageService.processAndPersistSMSResult(m);
        });
        logger.info(log.toString());
        return ResponseEntity.ok(log);
    }
}
