package org.devgateway.toolkit.forms.wicket;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import java.security.SecureRandom;
import java.util.Base64;

public class CustomRequestCycleListener implements IRequestCycleListener {
    public static final MetaDataKey<Boolean> ENDING_REQUEST = new MetaDataKey<Boolean>() {
        private static final long serialVersionUID = 1L;
    };
    public CustomRequestCycleListener() {
    }
    @Override
    public void onBeginRequest(RequestCycle cycle) {
        String nonce = generateNonce();
        cycle.setMetaData(ENDING_REQUEST, false);
        cycle.setMetaData(new MetaDataKey<>() {
        }, nonce);
        HttpServletResponse response = (HttpServletResponse)
                cycle.getResponse().getContainerResponse();
        response.setHeader("Content-Security-Policy",
                "script-src 'self' 'nonce-" + nonce + "'");
    }

    private String generateNonce() {
        byte[] nonceBytes = new byte[16];
        new SecureRandom().nextBytes(nonceBytes);
        return Base64.getEncoder().encodeToString(nonceBytes);
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        cycle.setMetaData(ENDING_REQUEST, true);
    }


    @Override
    public void onDetach(RequestCycle cycle) {
        // This method is called when the request cycle is detached
        System.out.println("Request cycle is being detached.");
    }

    @Override
    public IRequestHandler onException(RequestCycle cycle, Exception ex) {
        // This method is called if an exception occurs during the request cycle
        System.out.println("An exception occurred: " + ex.getMessage());
        return null;
    }
}
