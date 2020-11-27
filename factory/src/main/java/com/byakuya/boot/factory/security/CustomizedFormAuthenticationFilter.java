package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.exception.CustomizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ganzl on 2020/4/3.
 * 待验证码过滤器
 */
public class CustomizedFormAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (isEnableCaptcha()) {
            try {
                TextCaptcha.verifyCaptcha(request);
            } catch (CustomizedException e) {
                throw new BadCaptchaException(messages.getMessage(e.getExceptionCode(), e.getArgs()), e);
            }
        }
        return super.attemptAuthentication(request, response);
    }

    public boolean isEnableCaptcha() {
        return enableCaptcha;
    }

    public void setEnableCaptcha(boolean enableCaptcha) {
        this.enableCaptcha = enableCaptcha;
    }
    private boolean enableCaptcha = false;
}
