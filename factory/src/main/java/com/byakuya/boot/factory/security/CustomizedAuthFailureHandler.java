package com.byakuya.boot.factory.security;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ganzl on 2020/12/10.
 */
public class CustomizedAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    CustomizedAuthFailureHandler(String defaultFailureUrl, String changePasswordUrl) {
        super(defaultFailureUrl);
        Assert.isTrue(UrlUtils.isValidRedirectUrl(changePasswordUrl),
                () -> "'" + changePasswordUrl + "' is not a valid redirect URL");
        this.changePasswordUrl = changePasswordUrl;
        this.failureUrlBackup = defaultFailureUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof CredentialsExpiredException && StringUtils.hasText(this.changePasswordUrl)) {
            String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
            String redirectUrl = this.changePasswordUrl;
            if (StringUtils.hasText(username)) {
                redirectUrl += "?" + UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY + "=" + username;
            }
            this.setSuperFailureUrl(redirectUrl);
        } else {
            this.setSuperFailureUrl(this.failureUrlBackup);
        }
        super.onAuthenticationFailure(request, response, exception);
    }

    private void setSuperFailureUrl(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
        this.failureUrlBackup = defaultFailureUrl;
    }

    private String changePasswordUrl;
    private String failureUrlBackup;
}
