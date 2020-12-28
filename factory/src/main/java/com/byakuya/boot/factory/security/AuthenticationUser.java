package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.config.property.SecurityProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

/**
 * Created by ganzl on 2020/12/10.
 */
public class AuthenticationUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    AuthenticationUser(String userId
            , String username
            , String password
            , boolean enabled
            , boolean accountNonExpired
            , boolean credentialsNonExpired
            , boolean accountNonLocked
            , CustomizedGrantedAuthority authority) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, AuthorityUtils.NO_AUTHORITIES);
        this.userId = userId;
        this.admin = false;
        this.authority = authority;
    }

    AuthenticationUser(SecurityProperties.Admin admin) {
        super(admin.getUsername(), admin.getPassword(), AuthorityUtils.NO_AUTHORITIES);
        this.userId = null;
        this.admin = true;
        this.authority = CustomizedGrantedAuthority.SUPER;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AuthorityUtils.NO_AUTHORITIES;
    }

    public CustomizedGrantedAuthority getAuthority() {
        return authority;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isAdmin() {
        return admin;
    }

    private final boolean admin;
    private final CustomizedGrantedAuthority authority;
    private final String userId;
}
