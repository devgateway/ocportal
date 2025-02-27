package org.devgateway.toolkit.forms.filters;

import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Some resources from the UI (react) app that are located at /ui/static/** can be cached forever because the URL
 * contains the hash of the contents. If content changes, then the URL will change as well.
 *
 * @author Octavian Ciubotaru
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UICacheControlFilter extends ExpiresFilter {

    private final AntPathMatcher matcher = new AntPathMatcher();

    /**
     * 8 characters between dots is a sign that the resource URL contains the content hash.
     */
    private final String[] patterns = new String[] {
            "/ui/static/css/?*.????????.chunk.css",
            "/ui/static/js/?*.????????.chunk.js",
            "/ui/static/media/?*.????????.?*",
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);

        List<Duration> durations = new ArrayList<>();
        durations.add(new Duration(1, DurationUnit.YEAR));

        setDefaultExpiresConfiguration(new ExpiresConfiguration(StartingPoint.ACCESS_TIME, durations));
    }

    @Override
    protected boolean isEligibleToExpirationHeaderGeneration(HttpServletRequest request,
            XHttpServletResponse response) {
        boolean isEligible = isHashedURI(request.getRequestURI())
                && super.isEligibleToExpirationHeaderGeneration(request, response);
        if (isEligible) {
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-transform");
        }
        return isEligible;
    }

    private boolean isHashedURI(String path) {
        if (path == null) {
            return false;
        }
        for (String pattern : patterns) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
