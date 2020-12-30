package com.byakuya.boot.factory.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by ganzl on 2020/12/30.
 */
@Slf4j
@Component
public class TriColorLED {
    @JmsListener(destination = "TriColorLED.status")
    public void subscriber(String text) {
        log.warn("三色灯状态:" + text);
    }

}
