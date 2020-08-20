package org.devgateway.toolkit.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.devgateway.toolkit.web.security.SecurityConstants.JWTConstants.HEADER_STRING;
import static org.devgateway.toolkit.web.security.SecurityConstants.JWTConstants.TOKEN_PREFIX;

/**
 * https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final String jwtFilter;

    public JWTAuthorizationFilter(AuthenticationManager authManager, String jwtFilter) {
        super(authManager);
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }


    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = JWT.require(Algorithm.HMAC512(jwtFilter.getBytes()))
                    .build().verify(token.replace(TOKEN_PREFIX, "")).getSubject();
            if (user != null) {
                // new arraylist means authorities
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}