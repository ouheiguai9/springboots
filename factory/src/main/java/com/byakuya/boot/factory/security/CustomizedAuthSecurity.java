package com.byakuya.boot.factory.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.byakuya.boot.factory.ConstantUtils;
import com.byakuya.boot.factory.config.property.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/18.
 */
@Component
@Slf4j
public class CustomizedAuthSecurity {
    public CustomizedAuthSecurity(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public boolean checkAuthApiUrl(Authentication authentication, HttpServletRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        if (securityProperties.isOpenApiJwt()) {
            String token = request.getHeader(ConstantUtils.AUTH_PAGE_TOKEN_KEY);
            if (!StringUtils.hasText(token)) return false;
            try {
                DecodedJWT jwt = JWT.decode(token);
                JWT.require(createAlgorithm(request)).build().verify(jwt);
                return check(authentication, request, jwt.getClaim(moduleKey).asString(), jwt.getClaim(componentKey).asString());
            } catch (JWTVerificationException exception) {
                return false;
            }
        }
        return true;
    }

    private boolean check(Authentication authentication, HttpServletRequest request, String module, String component) {
        return Optional.of(authentication)
                .map(Authentication::getPrincipal)
                .filter(x -> x instanceof AuthenticationUser)
                .map(AuthenticationUser.class::cast).map(user -> {
                    log.info("用户:\t{}, 访问:\t{}", user.getUserId(), request.getRequestURI());
                    return user.isAdmin() || user.getAuthority().check(module, component);
                }).orElse(false);
    }

    public boolean checkAuthPageUrl(Authentication authentication, HttpServletRequest request, String module, String component) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        boolean rtnVal = check(authentication, request, module, component);
        if (rtnVal && securityProperties.isOpenApiJwt()) {
            request.setAttribute(ConstantUtils.AUTH_PAGE_TOKEN_KEY, createToken(request, module, component));
        }
        return rtnVal;
    }

    private String createToken(HttpServletRequest request, String module, String component) {
        return JWT.create()
                .withClaim(moduleKey, module)
                .withClaim(componentKey, component)
                .sign(createAlgorithm(request));
    }

    private Algorithm createAlgorithm(HttpServletRequest request) {
        return Algorithm.HMAC256(request.getSession().getId());
    }

    private final String componentKey = "component";
    private final String moduleKey = "module";
    private final SecurityProperties securityProperties;
}
