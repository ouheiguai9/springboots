package com.byakuya.boot.factory.mqtt;

import com.byakuya.boot.factory.component.device.log.TriColorLedLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by ganzl on 2020/12/30.
 */
@Slf4j
@Component
public class TriColorLED {
    public TriColorLED(TriColorLedLogService triColorLedLogService) {
        this.triColorLedLogService = triColorLedLogService;
    }

    @JmsListener(destination = "TriColorLED.status")
    public void subscriber(ActiveMQBytesMessage message) {
        try {
            byte[] bytes = new byte[(int) message.getBodyLength()];
            message.readBytes(bytes);
            String status = new String(bytes, StandardCharsets.UTF_8);
            LocalDateTime inTime = LocalDateTime.ofInstant(new Date(message.getBrokerInTime()).toInstant(), ZoneId.systemDefault());
            triColorLedLogService.save(status, inTime);
        } catch (JMSException e) {
            log.error("TriColorLED.status订阅消息失败", e);
        }

    }

    private final TriColorLedLogService triColorLedLogService;
}
