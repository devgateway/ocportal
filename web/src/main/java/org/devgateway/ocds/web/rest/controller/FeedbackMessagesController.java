package org.devgateway.ocds.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.service.feedback.ReplyableFeedbackMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by mpostelnicu
 */
@RestController
public class FeedbackMessagesController {

    @Autowired
    private ReplyableFeedbackMessageService feedbackMessageService;

    @ApiOperation(value = "Returns a list of feedback messages for a given page")
    @RequestMapping(value = "/api/feedback", method = {RequestMethod.GET, RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ReplyableFeedbackMessage> feedback(final HttpServletResponse response,
                                                   @RequestParam String page) throws IOException {
        return feedbackMessageService.findByUrlAndVisibleTrue(page);
    }

}
