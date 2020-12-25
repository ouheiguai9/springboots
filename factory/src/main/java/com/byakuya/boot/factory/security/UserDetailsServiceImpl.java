package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.component.menu.Menu;
import com.byakuya.boot.factory.component.user.User;
import com.byakuya.boot.factory.component.user.UserService;
import com.byakuya.boot.factory.config.property.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by ganzl on 2020/4/9.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetailsServiceImpl(UserService userService, SecurityProperties securityProperties) {
        this.userService = userService;
        this.securityProperties = securityProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationUser rtnVal;
        SecurityProperties.Admin admin = securityProperties.getAdmin();
        if (admin.getUsername().equals(username)) {
            rtnVal = new AuthenticationUser(securityProperties.getAdmin());
        } else {
            User user = userService.loadUser(username).orElseThrow(() -> new UsernameNotFoundException(username));
            LocalDateTime now = LocalDateTime.now(), begin = user.getBeginValidPeriod(), end = user.getEndValidPeriod(), last = user.getLastPasswordModifiedDate();
            boolean isExpired = now.isBefore(begin) || (end != null && now.isAfter(end));
            boolean isExpiredPassword = last == null || now.isAfter(last.plusDays(securityProperties.getPasswordValidPeriod()));
            rtnVal = new AuthenticationUser(user.getId(), user.getUsername(), user.getPassword(), !user.isLocked(), !isExpired, !isExpiredPassword, !user.isLocked(), extractGrantedAuthority(user));
        }
        return rtnVal;
    }

    /**
     * 提取用户权限
     *
     * @param user 用户
     * @return 权限
     */
    private CustomizedGrantedAuthority extractGrantedAuthority(User user) {
        Set<Menu> menuSet = new HashSet<>(user.getMenuSet());
        Optional.ofNullable(user.getRoleSet()).ifPresent(roles -> roles.forEach(role -> {
            if (!role.isLocked()) menuSet.addAll(role.getMenuSet());
        }));
        Map<String, Set<String>> authorityMap = new HashMap<>();
        menuSet.stream().filter(x -> x.getParentId().isPresent()).forEach(menu -> {
            String module = menu.getParent().getCode();
            Set<String> exists = authorityMap.computeIfAbsent(module, k -> new HashSet<>());
            exists.add(menu.getCode());
        });
        return new CustomizedGrantedAuthority(authorityMap);
    }

    private final SecurityProperties securityProperties;
    private final UserService userService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// 使用 BCrypt 加密
    }
}
