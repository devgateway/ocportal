/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:CacheCo
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms;

import org.devgateway.toolkit.persistence.spring.CustomJPAUserDetailsService;
import org.devgateway.toolkit.web.spring.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@Order(1)
public class FormsSecurityConfig extends WebSecurityConfig {

    /**
     * Remember me key for {@link TokenBasedRememberMeServices}
     */


    @Autowired
    protected CustomJPAUserDetailsService customJPAUserDetailsService;



    /**
     * This bean defines the same key in the
     * {@link RememberMeAuthenticationProvider}
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
        TokenBasedRememberMeServices rememberMeServices =
                new TokenBasedRememberMeServices(UNIQUE_SECRET_REMEMBER_ME_KEY, customJPAUserDetailsService);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    /**
     * We ensure the superclass configuration is being applied Take note the
     * {@link FormsSecurityConfig} extends {@link WebSecurityConfig} which has
     * configuration for the dg-toolkit/web module. We then apply ant matchers
     * and ignore security for css/js/images resources, and wicket mounted
     * resources
     */
    @Bean
    public SecurityFilterChain formsSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityContext(securityContext ->
                        securityContext.securityContextRepository(httpSessionSecurityContextRepository())
                )
                .securityMatcher("/resources/**", "/wicket/**", "/monitoring/**", "/login") // Restrict scope
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/monitoring/**").hasRole("ADMIN") // Restrict monitoring
                        .requestMatchers(
                                "/wicket/resource/**/*.js",
                                "/wicket/resource/**/*.css",
                                "/wicket/resource/**/*.css.map",
                                "/wicket/resource/**/*.png",
                                "/wicket/resource/**/*.jpg",
                                "/wicket/resource/**/*.woff",
                                "/wicket/resource/**/*.woff2",
                                "/wicket/resource/**/*.ttf",
                                "/wicket/resource/**/*.gif",
                                "/resources/public/**"
                        ).permitAll()
                )
                .formLogin(form ->
                        form.loginPage("/login").permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .anonymous(AbstractHttpConfigurer::disable) // Disallow anonymous access
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                        .contentTypeOptions(Customizer.withDefaults())
                        .xssProtection(Customizer.withDefaults())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("script-src 'self' "
                                        + "'unsafe-inline' 'strict-dynamic' style-src 'self'"))
                        .cacheControl(Customizer.withDefaults())
                );

        return http.build();
    }
}
