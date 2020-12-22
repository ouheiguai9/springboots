package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.component.menu.MenuRepository;
import com.byakuya.boot.factory.component.user.SecurityUser;
import com.byakuya.boot.factory.component.user.SecurityUserService;
import com.byakuya.boot.factory.config.property.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by ganzl on 2020/4/9.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetailsServiceImpl(MenuRepository menuRepository, SecurityUserService securityUserService, SecurityProperties securityProperties) {
        this.menuRepository = menuRepository;
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
            SecurityUser user = securityUserService.loadUser(username).orElseThrow(() -> new UsernameNotFoundException(username));
            rtnVal = new AuthenticationUser(user, securityProperties.getPasswordValidPeriod());
        }
        return rtnVal;
    }
    private final MenuRepository menuRepository;
    private final SecurityProperties securityProperties;
    private final SecurityUserService securityUserService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// 使用 BCrypt 加密
    }
}
