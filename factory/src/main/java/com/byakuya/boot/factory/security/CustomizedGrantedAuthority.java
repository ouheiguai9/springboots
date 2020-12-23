package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.SystemVersion;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

/**
 * Created by ganzl on 2020/12/18.
 */
public class CustomizedGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    static final CustomizedGrantedAuthority SUPER = new SuperAuthority();

    CustomizedGrantedAuthority(Map<String, Set<String>> authorityMap) {
        //noinspection unchecked
        this.authorityMap = authorityMap == null || authorityMap.isEmpty() ? Collections.EMPTY_MAP : new HashMap<>(authorityMap);
    }

    /**
     * 校验功能是否授权
     *
     * @param module    模块
     * @param component 组件
     * @return 是否授权
     */
    boolean check(String module, String component) {
        return Optional.ofNullable(authorityMap.get(module)).map(x -> x.contains(component)).orElse(false);
    }

    @Override
    public String getAuthority() {
        return null;
    }

    private final Map<String, Set<String>> authorityMap;

    private static class SuperAuthority extends CustomizedGrantedAuthority {
        private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

        SuperAuthority() {
            super(null);
        }

        @Override
        boolean check(String module, String component) {
            return true;
        }
    }
}
