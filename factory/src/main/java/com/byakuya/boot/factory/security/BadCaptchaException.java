package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.SystemVersion;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by ganzl on 2020/4/7.
 * 验证码校验失败异常
 */
public class BadCaptchaException extends AuthenticationException {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public BadCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }
}
