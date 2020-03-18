package org.devgateway.toolkit.persistence.dao.sms;

import java.io.Serializable;
import java.util.List;

public class SMSMessageWrapper implements Serializable {
    private Integer messageCount;
    private Integer pendingMessageCount;

    private List<SMSMessage> results;

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Integer getPendingMessageCount() {
        return pendingMessageCount;
    }

    public void setPendingMessageCount(Integer pendingMessageCount) {
        this.pendingMessageCount = pendingMessageCount;
    }

    public List<SMSMessage> getResults() {
        return results;
    }

    public void setResults(List<SMSMessage> results) {
        this.results = results;
    }
}
