package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.component.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Created by ganzl on 2020/12/17.
 */
public class SpringSecurityAuditorAware implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(AuthenticationUser.class::cast).map(AuthenticationUser::getRealUser).map(x -> x.orElse(null));
    }
}
