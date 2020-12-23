package com.byakuya.boot.factory.config;

import com.byakuya.boot.factory.ConstantUtils;
import com.byakuya.boot.factory.exception.FileIOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

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
        File upload = Paths.get(ConstantUtils.UPLOAD_DIR).toAbsolutePath().normalize().toFile();
        if (!upload.exists() || !upload.isDirectory()) {
            upload.deleteOnExit();
            if (!upload.mkdirs()) {
                throw new FileIOException();
            }
        }
        String location = String.format("file:%s%s", upload.getAbsolutePath(), File.separator);
        log.warn("文件上传路径:{}", location);
        registry.addResourceHandler(ConstantUtils.ADDITIONAL_RESOURCE_PATH + "**").addResourceLocations(location);
    }
}
