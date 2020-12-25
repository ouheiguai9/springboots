package com.byakuya.boot.factory.api;

import com.byakuya.boot.factory.config.OpenRestAPIController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by ganzl on 2020/12/25.
 */
@Slf4j
@OpenRestAPIController(path = {"leds"})
public class TriColorLEDController {
    @PostMapping("/status")
    public ResponseEntity<String> receiveStatus(@RequestBody String status) {
        log.warn("接收到状态:{}", status);
        return ResponseEntity.ok(status);
    }
}
