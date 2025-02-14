package org.devgateway.toolkit.forms.wicket.components.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.wicket.request.cycle.RequestCycle;


/**
 * @author Nadejda Mandrescu
 */
public final class PageUtil {
    private PageUtil() {
    }

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) RequestCycle.get().getResponse().getContainerResponse();
    }

}
