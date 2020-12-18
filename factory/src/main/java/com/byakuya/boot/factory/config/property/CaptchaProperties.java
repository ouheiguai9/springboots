package com.byakuya.boot.factory.config.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ganzl on 2020/4/7.
 * 图片验证码配置信息
 */
@Data
@Component
@ConditionalOnProperty(prefix = "system.captcha", name = "enable", havingValue = "true", matchIfMissing = true)
@ConfigurationProperties(prefix = "system.captcha")
public class CaptchaProperties {
    private String candidateChar = "0123456789";
    private boolean enable = false;
    private String fontColor = "black";
    private String fontFamily = "Arial, Courier";
    private int fontSize = 60;
    private int imageHeight = 100;
    private int imageWidth = 200;
    private int length = 4;
    private String noiseColor = "black";
    private int validSecond = 24 * 3600;
}
