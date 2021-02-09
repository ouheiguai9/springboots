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

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by ganzl on 2020/4/9.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetailsServiceImpl(UserService userService, SecurityProperties securityProperties, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.securityProperties = securityProperties;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties.getAdmin().setPassword(adjust(this.securityProperties.getAdmin().getPassword()));
    }

    /**
     * 如果配置为随机密码则每次启动随机生成超管密码
     *
     * @param password 原始密码
     * @return 新密码
     */
    private String adjust(String password) {
        if (!"random".equals(password)) return password;
        String numbers = "01234567890";
        //noinspection SpellCheckingInspection
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String symbols = "~!@#$^*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int totalSize = random.nextInt(6) + 10;
        for (int i = 0; i < totalSize; i++) {
            char c = letters.charAt(random.nextInt(letters.length()));
            if (random.nextBoolean()) {
                c = Character.toUpperCase(c);
            }
            sb.append(c);
        }
        for (int i = 0; i < 3; i++) {
            sb.setCharAt(random.nextInt(totalSize), numbers.charAt(random.nextInt(numbers.length())));
        }
        sb.setCharAt(random.nextInt(totalSize), symbols.charAt(random.nextInt(symbols.length())));
        System.out.println("\n\n\n");
        System.out.print("Random Password: ");
        System.out.print(sb);
        System.out.println("\n\n\n");
        return this.passwordEncoder.encode(sb.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationUser rtnVal;
        SecurityProperties.Admin admin = securityProperties.getAdmin();
        if (admin.getUsername().equals(username)) {
            rtnVal = new AuthenticationUser(securityProperties.getAdmin());
        } else {
            User user = userService.loadUser(username).orElseThrow(() -> new UsernameNotFoundException(username));
            LocalDateTime begin = user.getBeginValidPeriod(), now = LocalDateTime.now(), end = user.getEndValidPeriod(), last = user.getLastPasswordModifiedDate();
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
        menuSet.forEach(menu -> {
            String module = menu.getParent().getCode();
            Set<String> exists = authorityMap.computeIfAbsent(module, k -> new HashSet<>());
            exists.add(menu.getCode());
        });
        return new CustomizedGrantedAuthority(authorityMap);
    }

    private final SecurityProperties securityProperties;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// 使用 BCrypt 加密
    }
}
