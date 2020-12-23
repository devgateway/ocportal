package org.devgateway.toolkit.forms.filters;

import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Some resources from the UI (react) app that are located at /ui/static/** can be cached forever because the URL
 * contains the hash of the contents. If content changes, then the URL will change as well.
 *
 * @author Octavian Ciubotaru
 */
public class UICacheControlFilter implements Filter {

    private static final String CACHE_CONTROL = "Cache-Control";

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
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (isHashedURI(httpRequest.getRequestURI())) {
            chain.doFilter(request, new Wrapper((HttpServletResponse) response));
        } else {
            chain.doFilter(request, response);
        }
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

    /**
     * Sets Cache-Control header and disallows any downstream calls to setHeader/addHeader that attempt to modify the
     * said header.
     */
    private static class Wrapper extends HttpServletResponseWrapper {

        /**
         * Constructs a response adaptor wrapping the given response.
         *
         * @param response The response to be wrapped
         * @throws IllegalArgumentException if the response is null
         */
        public Wrapper(HttpServletResponse response) {
            super(response);
            super.setHeader(CACHE_CONTROL, "private, max-age=31536000");
        }

        @Override
        public void setHeader(String name, String value) {
            if (!name.equals(CACHE_CONTROL)) {
                super.setHeader(name, value);
            }
        }

        @Override
        public void addHeader(String name, String value) {
            if (!name.equals(CACHE_CONTROL)) {
                super.addHeader(name, value);
            }
        }
    }
}
