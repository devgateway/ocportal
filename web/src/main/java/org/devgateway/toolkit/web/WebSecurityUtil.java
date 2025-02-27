/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.web;

import jakarta.servlet.http.HttpServletRequest;

public class WebSecurityUtil {

    protected WebSecurityUtil() {

    }


    public static String createURL(final HttpServletRequest request, final String resourcePath) {
        final int port = request.getServerPort();
        final StringBuilder result = new StringBuilder();

        result.append(request.getScheme())
                .append("://")
                .append(request.getServerName());

        if ((request.getScheme().equals("http") && port != 80)
                || (request.getScheme().equals("https") && port != 443)) {
            result.append(':')
                    .append(port);
        }

        result.append(request.getContextPath());

        if (resourcePath != null && resourcePath.length() > 0) {
            if (!resourcePath.startsWith("/")) {
                result.append("/");
            }
            result.append(resourcePath);
        }

        return result.toString();
    }
}
