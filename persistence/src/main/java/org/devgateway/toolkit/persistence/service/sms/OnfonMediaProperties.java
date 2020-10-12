package org.devgateway.toolkit.persistence.service.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Octavian Ciubotaru
 */
@ConfigurationProperties("onfonmedia")
public class OnfonMediaProperties {

    private String baseURL = "https://api.onfonmedia.co.ke/v1/sms/SendBulkSMS";

    private String senderId = "Makueni";

    private String clientId;

    private String apiKey;

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
