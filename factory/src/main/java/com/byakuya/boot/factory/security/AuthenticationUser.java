package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.user.User;
import com.byakuya.boot.factory.config.property.SecurityProperties;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/10.
 */
public class AuthenticationUser implements UserDetails, CredentialsContainer {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    AuthenticationUser(User realUser, CustomizedGrantedAuthority authority, long passwordValidPeriod) {
        this.admin = null;
        this.realUser = realUser;
        this.password = realUser.getPassword();
        this.passwordValidPeriod = passwordValidPeriod;
        this.authority = authority;
    }

    AuthenticationUser(SecurityProperties.Admin admin) {
        this.realUser = null;
        this.passwordValidPeriod = Long.MAX_VALUE;
        this.admin = admin;
        this.password = admin.getPassword();
        this.authority = CustomizedGrantedAuthority.SUPER;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.NO_AUTHORITIES;
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
        if (isAdmin() || realUser.getEndValidPeriod() == null) return true;
        if (realUser.getBeginValidPeriod() == null) return false;
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(realUser.getBeginValidPeriod()) && now.isBefore(realUser.getEndValidPeriod());
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

    CustomizedGrantedAuthority getAuthority() {
        return authority;
    }

    public String getNickname() {
        if (isAdmin()) return admin.getNickname();
        return realUser.getNickname();
    }

    Optional<User> getRealUser() {
        return Optional.ofNullable(realUser);
    }

    private final SecurityProperties.Admin admin;
    private final CustomizedGrantedAuthority authority;
    private final long passwordValidPeriod;
    private final User realUser;
    private String password;
}
