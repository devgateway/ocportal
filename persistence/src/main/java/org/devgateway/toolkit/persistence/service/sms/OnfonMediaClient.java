package org.devgateway.toolkit.persistence.service.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class OnfonMediaClient {

    private final Logger logger = LoggerFactory.getLogger(OnfonMediaClient.class);

    @Autowired
    private OnfonMediaProperties properties;

    public static class BulkSMSRequest {

        @JsonProperty("ApiKey")
        private final String apiKey;

        @JsonProperty("ClientId")
        private final String clientId;

        @JsonProperty("SenderId")
        private final String senderId;

        @JsonProperty("MessageParameters")
        private final List<Message> messageParameters;

        public BulkSMSRequest(String apiKey, String clientId, String senderId,
                List<Message> messageParameters) {
            this.apiKey = apiKey;
            this.clientId = clientId;
            this.senderId = senderId;
            this.messageParameters = messageParameters;
        }

        public String getApiKey() {
            return apiKey;
        }

        public String getClientId() {
            return clientId;
        }

        public String getSenderId() {
            return senderId;
        }

        public List<Message> getMessageParameters() {
            return messageParameters;
        }
    }

    private class LoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] requestBody,
                ClientHttpRequestExecution execution) throws IOException {

            logger.info(">> {} {}\n{}", request.getMethodValue(), request.getURI(),
                    new String(requestBody, StandardCharsets.UTF_8));

            ClientHttpResponse response = execution.execute(request, requestBody);

            InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
            String responseBody = new BufferedReader(isr)
                    .lines()
                    .collect(Collectors.joining("\n"));
            logger.info("<< {} {}\n{}", response.getRawStatusCode(), response.getStatusText(), responseBody);

            return response;
        }
    }

    public void sendBulkSMS(List<Message> messages) {
        BulkSMSRequest request = new BulkSMSRequest(properties.getApiKey(), properties.getClientId(),
                properties.getSenderId(), messages);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getInterceptors().add(new LoggingInterceptor());
        restTemplate.postForObject(properties.getBaseURL() + "/v1/sms/SendBulkSMS", request, Map.class);
    }
}
