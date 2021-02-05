package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.ConstantUtils;
import com.byakuya.boot.factory.config.property.CaptchaProperties;
import com.byakuya.boot.factory.config.property.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Created by ganzl on 2020/4/3.
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public WebSecurityConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //图片
        web.ignoring().mvcMatchers("/image/**");
        //样式表
        web.ignoring().mvcMatchers("/css/**");
        //脚本
        web.ignoring().mvcMatchers("/js/**");
        //插件
        web.ignoring().mvcMatchers("/plugin/**");
        //验证码
        web.ignoring().mvcMatchers("/captcha");
        //错误页面
        web.ignoring().mvcMatchers("/error/**");
        //注册
        web.ignoring().mvcMatchers("/register");
        //开放API
        web.ignoring().mvcMatchers(ConstantUtils.OPEN_REST_API_PREFIX + "/**");
        web.ignoring().mvcMatchers(changePasswordUrl);   //修改密码
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginPageUrl = securityProperties.getLoginPageUrl();
        String loginProcessingUrl = securityProperties.getLoginProcessingUrl();
        AuthenticationFailureHandler failureHandler = new CustomizedAuthFailureHandler(loginPageUrl, changePasswordUrl);

        http
                .authorizeRequests()
                .mvcMatchers("/auth/page/{module}/{component}/**").access("@customizedAuthSecurity.checkAuthPageUrl(authentication, request, #module, #component)")
                .mvcMatchers("/auth/api/**").access("@customizedAuthSecurity.checkAuthApiUrl(authentication, request)")
                .anyRequest().authenticated()
                .and()
                .apply(new CustomizedFormLoginConfigurer<>()).setEnableCaptcha(captchaProperties != null).loginPage(loginPageUrl).loginProcessingUrl(loginProcessingUrl).failureHandler(failureHandler).permitAll()
                //.formLogin().loginPage(loginPageUrl).loginProcessingUrl(loginProcessingUrl).failureHandler(failureHandler).permitAll()
                .and()
                .logout().logoutSuccessUrl(loginPageUrl).invalidateHttpSession(true)
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin();
    }

    @Autowired(required = false)
    public void setCaptchaProperties(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    private final String changePasswordUrl = "/changePassword";
    private CaptchaProperties captchaProperties;
    private SecurityProperties securityProperties;
}
