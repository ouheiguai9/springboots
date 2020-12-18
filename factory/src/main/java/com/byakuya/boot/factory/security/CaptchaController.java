package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.config.property.CaptchaProperties;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * Created by ganzl on 2020/4/8.
 * 验证码控制器
 */
@RestController
@ConditionalOnBean(CaptchaProperties.class)
public class CaptchaController {

    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @GetMapping(value = "captcha", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage captcha(HttpServletRequest request) {
        TextCaptcha textCaptcha = TextCaptcha.createCaptcha(request, defaultKaptcha.createText(), captchaProperties.getValidSecond());
        return defaultKaptcha.createImage(textCaptcha.text);
    }

    @Autowired
    public void setCaptchaProperties(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    @Autowired
    public void setDefaultKaptcha(DefaultKaptcha defaultKaptcha) {
        this.defaultKaptcha = defaultKaptcha;
    }

    private CaptchaProperties captchaProperties;
    private DefaultKaptcha defaultKaptcha;

    @Bean
    public static DefaultKaptcha getDefaultKaptcha(CaptchaProperties captchaProperties) {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_BORDER, "no");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, captchaProperties.getCandidateChar());
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, captchaProperties.getFontColor());
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, String.valueOf(captchaProperties.getImageWidth()));
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, String.valueOf(captchaProperties.getImageHeight()));
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, String.valueOf(captchaProperties.getLength()));
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, captchaProperties.getFontFamily());
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, String.valueOf(captchaProperties.getFontSize()));
        properties.setProperty(Constants.KAPTCHA_NOISE_COLOR, captchaProperties.getNoiseColor());
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
