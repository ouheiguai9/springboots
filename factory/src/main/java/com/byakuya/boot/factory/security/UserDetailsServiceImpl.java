package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.component.user.SecurityUser;
import com.byakuya.boot.factory.component.user.SecurityUserService;
import com.byakuya.boot.factory.property.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by ganzl on 2020/4/9.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetailsServiceImpl(SecurityUserService securityUserService, SecurityProperties securityProperties) {
        this.securityUserService = securityUserService;
        this.securityProperties = securityProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationUser rtnVal;
        SecurityProperties.Admin admin = securityProperties.getAdmin();
        if (admin.getUsername().equals(username)) {
            rtnVal = new AuthenticationUser(securityProperties.getAdmin());
        } else {
            Optional<SecurityUser> optionalUser = securityUserService.loadUser(username);
            if (!optionalUser.isPresent()) {
                throw new UsernameNotFoundException(username);
            }
            rtnVal = new AuthenticationUser(optionalUser.get(), securityProperties.getPasswordValidPeriod());
        }
        return rtnVal;
    }

    private final SecurityProperties securityProperties;
    private final SecurityUserService securityUserService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// 使用 BCrypt 加密
    }
}
