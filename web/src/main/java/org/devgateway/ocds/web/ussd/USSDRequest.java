package org.devgateway.ocds.web.ussd;

import java.util.Arrays;

/**
 * @author Octavian Ciubotaru
 */
public class USSDRequest {

    private final String msisdn;

    private final String[] path;

    public USSDRequest(String msisdn, String[] path) {
        this.msisdn = msisdn;
        this.path = path;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String[] getPath() {
        return path;
    }

    public USSDRequest shift() {
        return new USSDRequest(msisdn, Arrays.copyOfRange(path, 1, path.length));
    }
}
