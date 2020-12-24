package com.byakuya.boot.factory.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by ganzl on 2020/12/18.
 */
@Component
@Slf4j
public class CustomizedWebSecurity {
    public boolean checkAuthPageUrl(Authentication authentication, String module, String component) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        return Optional.of(authentication)
                .map(Authentication::getPrincipal)
                .filter(x -> x instanceof AuthenticationUser)
                .map(AuthenticationUser.class::cast).map(user -> {
                    log.info("用户:\t{}, 访问模块:\t{}, 访问组件:\t {}", user.getUserId(), module, component);
                    return user.isAdmin() || user.getAuthority().check(module, component);
                }).orElse(false);
    }
}
