package com.byakuya.boot.factory.security;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ganzl on 2020/11/26.
 */
public class CustomizedFormLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, CustomizedFormLoginConfigurer<H>, CustomizedFormAuthenticationFilter> {

    public CustomizedFormLoginConfigurer() {
        super(new CustomizedFormAuthenticationFilter(), null);
    }

    @Override
    public CustomizedFormLoginConfigurer<H> loginPage(String loginPage) {
        return super.loginPage(loginPage);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, loginProcessingRequestMethod.toString());
    }

    public RequestMethod getLoginProcessingRequestMethod() {
        return loginProcessingRequestMethod;
    }

    public CustomizedFormLoginConfigurer<H> setLoginProcessingRequestMethod(RequestMethod loginProcessingRequestMethod) {
        this.loginProcessingRequestMethod = loginProcessingRequestMethod;
        if (loginProcessingRequestMethod != RequestMethod.POST) {
            getAuthenticationFilter().setPostOnly(false);
        }
        return this;
    }

    public boolean isEnableCaptcha() {
        return getAuthenticationFilter().isEnableCaptcha();
    }

    public CustomizedFormLoginConfigurer<H> setEnableCaptcha(boolean enableCaptcha) {
        getAuthenticationFilter().setEnableCaptcha(enableCaptcha);
        return this;
    }

    private RequestMethod loginProcessingRequestMethod = RequestMethod.POST;
}
