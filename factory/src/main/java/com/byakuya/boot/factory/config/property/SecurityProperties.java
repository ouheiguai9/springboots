package com.byakuya.boot.factory.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

/**
 * Created by ganzl on 2020/6/23.
 */
@Data
@Component
@ConfigurationProperties(prefix = "system.security")
public class SecurityProperties {

    public long getPasswordValidPeriod() {
        return passwordValidPeriod < 1 ? 100000 : passwordValidPeriod;
    }

    private Admin admin;
    private boolean allowRegistration = true;
    private String loginPageUrl = "/login";
    private RequestMethod loginProcessingRequestMethod = RequestMethod.POST;
    private String loginProcessingUrl = "/login";
    private String newUserDefaultPassword = "abc!123";
    private boolean openApiJwt = false;
    private long passwordValidPeriod = 180;

    @Data
    public static class Admin implements Serializable {
        private String nickname = "系统管理员";
        private String password = "$2a$10$5zxDSXre318myTohrygW7OhbsElm76a1shCZk4qmCAmj9LX7ZhuyW";
        private String phoneNumber = "88888888888";
        private String username = "admin";
    }
}
