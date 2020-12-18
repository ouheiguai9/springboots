package com.byakuya.boot.factory.config;

import com.byakuya.boot.factory.ConstantUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by ganzl on 2020/12/18.
 */
@Configuration
public class CustomizedWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(ConstantUtils.AUTH_REST_API_PREFIX, cls -> cls.isAnnotationPresent(AuthRestAPIController.class));
    }
}
