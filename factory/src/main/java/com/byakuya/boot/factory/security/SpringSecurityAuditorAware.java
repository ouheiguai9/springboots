package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.component.user.User;
import com.byakuya.boot.factory.component.user.UserService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by ganzl on 2020/12/17.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<User> {
    public SpringSecurityAuditorAware(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(x -> x instanceof AuthenticationUser)
                .map(AuthenticationUser.class::cast)
                .filter(x -> !x.isAdmin())
                .map(x -> userService.get(x.getUserId()));
    }

    private final UserService userService;
}
