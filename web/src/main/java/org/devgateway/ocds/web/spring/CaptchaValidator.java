package org.devgateway.ocds.web.spring;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * From https://github.com/cristirosu/spring-boot-recaptcha-aop
 */
@Service
public class CaptchaValidator {

    public static class CaptchaResponse {

        private Boolean success;
        private Date timestamp;
        private String hostname;
        @JsonProperty("error-codes")
        private List<String> errorCodes;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public List<String> getErrorCodes() {
            return errorCodes;
        }

        public void setErrorCodes(List<String> errorCodes) {
            this.errorCodes = errorCodes;
        }
    }

    private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    public boolean validateCaptcha(String captchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", recaptchaSecret);
        requestMap.add("response", captchaResponse);

        CaptchaResponse apiResponse = restTemplate.postForObject(
                GOOGLE_RECAPTCHA_ENDPOINT, requestMap, CaptchaResponse.class);
        if (apiResponse == null) {
            return false;
        }

        return Boolean.TRUE.equals(apiResponse.getSuccess());
    }

}