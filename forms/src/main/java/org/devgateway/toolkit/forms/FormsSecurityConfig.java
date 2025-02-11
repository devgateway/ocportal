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
package org.devgateway.toolkit.forms;

import org.devgateway.toolkit.persistence.spring.CustomJPAUserDetailsService;
import org.devgateway.toolkit.web.security.SecurityUtil;
import org.devgateway.toolkit.web.spring.WebSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@Order(1) // this ensures the forms security comes first
public class FormsSecurityConfig extends WebSecurityConfig {

    /**
     * Remember me key for {@link TokenBasedRememberMeServices}
     */
    private static final String UNIQUE_SECRET_REMEMBER_ME_KEY = "secret";

    /**
     * We ensure the superclass configuration is being applied Take note the
     * {@link FormsSecurityConfig} extends {@link WebSecurityConfig} which has
     * configuration for the dg-toolkit/web module. We then apply ant matchers
     * and ignore security for css/js/images resources, and wicket mounted
     * resources
     */

    @Bean
    @Override
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        super.securityFilterChain(http);
        http.authorizeHttpRequests(auth->auth
                .requestMatchers("/portal/**", "/ui/**",
                        "/img/**", "/css*/**", "/js*/**", "/assets*/**", "/wicket/resource/**/*.js",
                        "/wicket/resource/**/*.css", "/wicket/resource/**/*.png", "/wicket/resource/**/*.jpg",
                        "/wicket/resource/**/*.woff", "/wicket/resource/**/*.woff2", "/wicket/resource/**/*.ttf",
                        "/favicon.ico", "/error",
                        "/wicket/resource/**/*.gif", "/login/**", "/forgotPassword/**", "/verifyEmail/**",
                        "/unsubscribeEmail/**", "/resources/**", "/resources/public/**")
                .permitAll()
                .anyRequest()
                .authenticated());

        return http.build();
    }



    /**
     * This bean defines the same key in the {@link RememberMeAuthenticationProvider}
     *
     * @return
     */
    @Bean
    public AuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(UNIQUE_SECRET_REMEMBER_ME_KEY);
    }

    /**
     * This bean configures the {@link TokenBasedRememberMeServices} with
     * {@link CustomJPAUserDetailsService}
     *
     * @return
     */
    @Bean
    public AbstractRememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
                UNIQUE_SECRET_REMEMBER_ME_KEY, customJPAUserDetailsService);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    @Override
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager)
            throws Exception {
        super.securityFilterChain(http, authenticationManager);
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                // Wicket manages sessions
                .csrf(AbstractHttpConfigurer::disable)
                // CSRF protection interferes with some Wicket functionality
                .headers(headers -> headers
                        .contentTypeOptions(withDefaults())
                        .xssProtection(withDefaults())
                        .cacheControl(withDefaults())
                        .httpStrictTransportSecurity(withDefaults())
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                        // Allow embedding in iframes from the same origin
                );

        return http.build();
    }
}
