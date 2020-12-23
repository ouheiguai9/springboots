package com.byakuya.boot.factory.config;

import com.byakuya.boot.factory.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Created by ganzl on 2020/12/18.
 */
@Slf4j
@Configuration
public class CustomizedWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(ConstantUtils.AUTH_REST_API_PREFIX, cls -> cls.isAnnotationPresent(AuthRestAPIController.class));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File upload = new File(ConstantUtils.UPLOAD_DIR);
        if (!(upload.exists() && upload.isDirectory())) {
            upload.deleteOnExit();
            if (!upload.mkdirs()) {
                throw new Error("上传目录无法创建");
            }
        }
        log.warn("文件上传路径:{}", upload.getPath());
        registry.addResourceHandler("/additional/**").addResourceLocations(String.format("file:%s/", upload.getPath()));
    }
}
