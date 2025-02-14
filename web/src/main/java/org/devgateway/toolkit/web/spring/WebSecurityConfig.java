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
package org.devgateway.toolkit.web.spring;

import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.persistence.spring.CustomJPAUserDetailsService;
import org.devgateway.toolkit.web.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

/**
 * @author mpostelnicu This configures the spring security for the Web project.
 * An
 */

@Configuration
@ConditionalOnMissingClass("org.devgateway.toolkit.forms.FormsSecurityConfig")
@Order(2) // this loads the security config after the forms security (if you use
// them overlayed, it must pick that one first)
@PropertySource("classpath:allowedApiEndpoints.properties")
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    protected CustomJPAUserDetailsService customJPAUserDetailsService;

    @Value("${allowedApiEndpoints}")
    private String[] allowedApiEndpoints;

    @Value("${roleHierarchy}")
    private String roleHierarchyStringRepresentation;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    protected AdminSettingsRepository adminSettingsRepository;
    protected static final String UNIQUE_SECRET_REMEMBER_ME_KEY = "secret";


    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        final StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityContextPersistenceFilter securityContextPersistenceFilter() {
        final SecurityContextPersistenceFilter securityContextPersistenceFilter =
                new SecurityContextPersistenceFilter(httpSessionSecurityContextRepository());
        return securityContextPersistenceFilter;
    }


    private String[] getAllowedAPIEndpointsWithBasePath() {
        if (allowedApiEndpoints != null) {
            return Arrays.stream(allowedApiEndpoints)
                    .toList().toArray(new String[allowedApiEndpoints.length]);
        }

        return new String[]{};
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityContext(securityContext ->
                        securityContext.securityContextRepository(httpSessionSecurityContextRepository()))
                .securityMatcher("/**") // Ensures this applies globally
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/v3/api-docs/**", "/swagger-ui.html**",
                                "/webjars/**", "/images/**", "/configuration/**",
                                "/swagger-resources/**", "/dashboard", "/languages/**",
                                "/isAuthenticated", "/error", "/swagger-ui/**",
                                "/corruption-risk",
                                "/login/**",
                                "/favicon.ico", "/error/**",
                                "/forgotPassword/**", "/verifyEmail/**",
                                "/unsubscribeEmail/**", "/resources/**",
                                "/portal/**", "/ui/**",
                                "/img/**", "/css*/**", "/js*/**", "/assets*/**"
                        ).permitAll()
                        .requestMatchers("/api/user/forgotPassword").permitAll()
                        .requestMatchers(allowedApiEndpoints).permitAll()
                        .requestMatchers("/api/**").access((authenticationSupplier, context) -> {
                                    Authentication authentication = authenticationSupplier.get();
                                    return new AuthorizationDecision(
                                            SecurityUtil.getDisabledApiSecurity(adminSettingsRepository)
                                            || (authentication != null && authentication.isAuthenticated()));
                                })
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                )
                .rememberMe(rememberMe ->
                        rememberMe.key(UNIQUE_SECRET_REMEMBER_ME_KEY).alwaysRemember(true)
                )
                .requestCache(RequestCacheConfigurer::disable)
                .logout(LogoutConfigurer::permitAll)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
            web.ignoring()
                    .requestMatchers(getAllowedAPIEndpointsWithBasePath())
                    .requestMatchers("/login",
                             "/forgotPassword/**");
        };
    }

    /**
     * Instantiates {@see DefaultWebSecurityExpressionHandler} and assigns to it
     * role hierarchy.
     *
     * @return
     */
    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        final DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }

    /**
     * Enable hierarchical roles. This bean can be used to extract all effective
     * roles.
     */
    @Bean
    RoleHierarchy roleHierarchy() {
        final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(roleHierarchyStringRepresentation);
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                                                   authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customJPAUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customJPAUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
