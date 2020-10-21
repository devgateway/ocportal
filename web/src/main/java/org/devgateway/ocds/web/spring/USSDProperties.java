package org.devgateway.ocds.web.spring;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Octavian Ciubotaru
 */
@ConfigurationProperties(prefix = "ussd", ignoreUnknownFields = false)
public class USSDProperties {

    /**
     * USSD Service Code. Basically the first part of the USSD string.
     * Examples of USSD strings as entered by user: *838# or *838*1#
     */
    private String serviceCode = "838";

    /**
     * This USSD prefix to be ignored because it was already processed. Options are separated by asterisk.
     * The prefix must not start nor end with an asterisk.
     */
    private String baseString = "7";

    private String endpointSecret = RandomStringUtils.randomAlphanumeric(16);

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getBaseString() {
        return baseString;
    }

    public void setBaseString(String baseString) {
        this.baseString = baseString;
    }

    public String getEndpointSecret() {
        return endpointSecret;
    }

    public void setEndpointSecret(String endpointSecret) {
        this.endpointSecret = endpointSecret;
    }
}
