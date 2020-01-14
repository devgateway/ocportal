package org.devgateway.ocds.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.feedback.ReplyableFeedbackMessageService;
import org.devgateway.toolkit.persistence.service.feedback.ReplyableFeedbackMessageServiceImpl;
import org.devgateway.toolkit.web.rest.controller.alerts.AlertsEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by mpostelnicu
 */
@RestController
@CacheConfig(cacheNames = "feedback")
public class FeedbackMessagesController {

    protected static final Logger logger = LoggerFactory.getLogger(ReplyableFeedbackMessageServiceImpl.class);

    @Autowired
    private ReplyableFeedbackMessageService feedbackMessageService;


    @Autowired
    private AlertsEmailService alertsEmailService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "Returns a list of feedback messages for a given page")
    @RequestMapping(value = "/api/feedback", method = {RequestMethod.GET, RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Cacheable
    public List<ReplyableFeedbackMessage> feedback(@RequestParam String page) {
        return feedbackMessageService.findByUrlAndVisibleTrue(page);
    }


    public static class FeedbackMessageSubmitWrapper implements Serializable {

        private String name;
        private String email;
        private String comment;
        private String url;
        private Long replyFor;
        private Long department;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getDepartment() {
            return department;
        }

        public void setDepartment(Long department) {
            this.department = department;
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
        fm.setAddedByPublic(true);

        if (message.getReplyFor() != null) {
            Optional<ReplyableFeedbackMessage> saveable = feedbackMessageService.findById(message.getReplyFor());
            if (saveable.isPresent()) {
                saveable.get().getReplies().add(fm);
                alertsEmailService.sendFeedbackAlertsForReplyable(saveable.get());
                feedbackMessageService.save(saveable.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            ReplyableFeedbackMessage rfm = (ReplyableFeedbackMessage) fm;

            Optional<Department> department = departmentService.findById(message.getDepartment());

            if (!department.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            rfm.setUrl(message.getUrl());
            rfm.setDepartment(department.get());
            feedbackMessageService.save(rfm);
            return ResponseEntity.ok().build();
        }
    }
}
