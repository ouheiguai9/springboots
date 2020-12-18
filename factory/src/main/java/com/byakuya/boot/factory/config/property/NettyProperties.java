package com.byakuya.boot.factory.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ganzl on 2020/11/27.
 */
@Data
@Component
@ConfigurationProperties(prefix = "system.netty")
public class NettyProperties {
    private int port;
}
