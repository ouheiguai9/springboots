package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.ConstantUtils;
import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.user.SecurityUser;
import com.byakuya.boot.factory.component.user.SecurityUserService;
import com.byakuya.boot.factory.property.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
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

    public static class AuthenticationUser implements UserDetails, CredentialsContainer {

        private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

        AuthenticationUser(SecurityUser realUser, long passwordValidPeriod) {
            this.admin = null;
            this.realUser = realUser;
            this.password = realUser.getPassword();
            this.passwordValidPeriod = passwordValidPeriod;
        }

        private AuthenticationUser(SecurityProperties.Admin admin) {
            this.realUser = null;
            this.passwordValidPeriod = Long.MAX_VALUE;
            this.admin = admin;
            this.password = admin.getPassword();
        }

        @Override
        public void eraseCredentials() {
            this.password = null;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return isAdmin() ? AuthorityUtils.createAuthorityList(ConstantUtils.ADMIN_USER_AUTHORITY) : AuthorityUtils.NO_AUTHORITIES;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return isAdmin() ? admin.getUsername() : realUser.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            if (isAdmin()) return true;
            return realUser.getBeginValidPeriod().isBefore(LocalDateTime.now()) && realUser.getEndValidPeriod().isAfter(LocalDateTime.now());
        }

        @Override
        public boolean isAccountNonLocked() {
            return isAdmin() || !realUser.isLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            if (isAdmin()) return true;
            return realUser.getLastPasswordModifiedDate() != null && realUser.getLastPasswordModifiedDate().plusDays(passwordValidPeriod).isAfter(LocalDateTime.now());
        }

        @Override
        public boolean isEnabled() {
            return isAdmin() || !realUser.isLocked();
        }

        public boolean isAdmin() {
            return admin != null;
        }

        public Optional<SecurityUser> getRealUser() {
            return Optional.ofNullable(realUser);
        }

        private final SecurityProperties.Admin admin;
        private final long passwordValidPeriod;
        private final SecurityUser realUser;
        private String password;
    }
}
