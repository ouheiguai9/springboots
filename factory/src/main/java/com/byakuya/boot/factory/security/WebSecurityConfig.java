package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.ConstantUtils;
import com.byakuya.boot.factory.property.CaptchaProperties;
import com.byakuya.boot.factory.property.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

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
        web.ignoring().mvcMatchers("/image/**");         //图片
        web.ignoring().mvcMatchers("/css/**");           //样式表
        web.ignoring().mvcMatchers("/js/**");            //脚本
        web.ignoring().mvcMatchers("/plugin/**");        //插件
        web.ignoring().mvcMatchers("/captcha");          //验证码
        web.ignoring().mvcMatchers("/error/**");         //错误页面
        web.ignoring().mvcMatchers("/register");         //注册
        web.ignoring().mvcMatchers("/changePassword");   //修改密码
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginPageUrl = securityProperties.getLoginPageUrl();
        String loginProcessingUrl = securityProperties.getLoginProcessingUrl();
        AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler(loginPageUrl);

        http
                .authorizeRequests()
                .antMatchers("/users").hasAnyAuthority(ConstantUtils.ADMIN_USER_AUTHORITY)
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .apply(new CustomizedFormLoginConfigurer<>()).setEnableCaptcha(captchaProperties != null).loginPage(loginPageUrl).loginProcessingUrl(loginProcessingUrl).failureHandler(failureHandler).permitAll()
                //.formLogin().loginPage(loginPageUrl).loginProcessingUrl(loginProcessingUrl).failureHandler(failureHandler).permitAll()
                .and()
                .logout().logoutSuccessUrl(loginPageUrl)
                .and()
                .httpBasic();
    }

    @Autowired(required = false)
    public void setCaptchaProperties(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    private CaptchaProperties captchaProperties;
    private SecurityProperties securityProperties;
}
