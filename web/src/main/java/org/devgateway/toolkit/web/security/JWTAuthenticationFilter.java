package org.devgateway.toolkit.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang.BooleanUtils;
import org.devgateway.toolkit.persistence.dao.Person;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.devgateway.toolkit.web.security.SecurityConstants.JWTConstants.AUTH_URL;
import static org.devgateway.toolkit.web.security.SecurityConstants.JWTConstants.EXPIRATION_TIME;

/**
 * https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final String jwtSecret;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, String jwtSecret) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(AUTH_URL);
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
            Map<String, String> map = objectMapper.readValue(req.getInputStream(), mapType);
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(map.get("username"), map.get("password"), new ArrayList<>())
            );
            if (authenticate != null) {
                Person person = (Person) authenticate.getPrincipal();
                if (!SecurityUtil.isUserPMCUser(person) || BooleanUtils.isFalse(person.isEnabled())) {
                    authenticate = null;
                }

            }
            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create().withSubject(((Person) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes()));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> mapResponse = new HashMap<>();
        Person p = (Person) auth.getPrincipal();
        mapResponse.put("id", p.getId());
        mapResponse.put("username", p.getUsername());
        mapResponse.put("firstname", p.getFirstName());
        mapResponse.put("lastname", p.getLastName());
        mapResponse.put("changePasswordNextSignIn", p.getChangePasswordNextSignIn());
        mapResponse.put("roles", p.getRoles());
        mapResponse.put("token", token);
        res.getWriter().write(objectMapper.writeValueAsString(mapResponse));
        res.getWriter().flush();
    }
}