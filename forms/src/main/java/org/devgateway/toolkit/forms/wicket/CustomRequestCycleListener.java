package org.devgateway.toolkit.forms.wicket;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

public class CustomRequestCycleListener implements IRequestCycleListener {
    public static final MetaDataKey<Boolean> ENDING_REQUEST = new MetaDataKey<Boolean>() {
        private static final long serialVersionUID = 1L;
    };
    public CustomRequestCycleListener()
    {
    }
    public void onBeginRequest(RequestCycle cycle) {
        cycle.setMetaData(ENDING_REQUEST, false);
    }

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
