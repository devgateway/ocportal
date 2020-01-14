package org.devgateway.ocds.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.service.feedback.ReplyableFeedbackMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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

    public class FeedbackMessageSubmitWrapper implements Serializable {

        private String name;
        private String email;
        private String comment;
        private Long replyFor;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Long getReplyFor() {
            return replyFor;
        }

        public void setReplyFor(Long replyFor) {
            this.replyFor = replyFor;
        }
    }

    @PostMapping(value = "/api/postFeedback",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postFeedback(@RequestBody FeedbackMessageSubmitWrapper message) {
        FeedbackMessage fm;
        if (message.getReplyFor() != null) {
            fm = new FeedbackMessage();
        } else {
            fm = new ReplyableFeedbackMessage();
        }

        fm.setEmail(message.getEmail());
        fm.setName(message.getName());
        fm.setComment(message.getComment());

        if (message.getReplyFor() != null) {
            Optional<ReplyableFeedbackMessage> saveable
                    = feedbackMessageService.findById(message.getReplyFor());
            if (saveable.isPresent()) {
                saveable.get().getReplies().add(fm);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            feedbackMessageService.save((ReplyableFeedbackMessage) fm);
            return ResponseEntity.ok().build();
        }
    }
}
