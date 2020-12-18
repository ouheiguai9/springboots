package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.SystemVersion;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by ganzl on 2020/12/18.
 */
public class CustomizedGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    /**
     * 校验功能是否授权
     *
     * @param module    模块
     * @param component 组件
     * @return 是否授权
     */
    public boolean check(String module, String component) {
        return true;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
