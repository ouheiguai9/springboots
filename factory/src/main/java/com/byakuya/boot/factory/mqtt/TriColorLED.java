package com.byakuya.boot.factory.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Created by ganzl on 2020/12/30.
 */
@Slf4j
@Component
public class TriColorLED {
    @JmsListener(destination = "TriColorLED.status")
    public void subscriber(String text) {
        String status = asciiToString(text);
        log.warn("三色灯状态:" + status);
    }

    private static String asciiToString(String value) {
        StringBuilder sbu = new StringBuilder();
        String[] chars = value.split(",");
        for (String aChar : chars) {
            sbu.append((char) Integer.parseInt(aChar));
        }
        return sbu.toString();
    }

}
