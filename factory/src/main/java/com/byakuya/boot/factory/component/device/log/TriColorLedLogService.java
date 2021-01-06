package com.byakuya.boot.factory.component.device.log;

import com.byakuya.boot.factory.component.device.Device;
import com.byakuya.boot.factory.component.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ganzl on 2021/1/5.
 */
@Service
public class TriColorLedLogService {
    private static final ConcurrentHashMap<String, Proxy> LAST_LOG_CACHE = new ConcurrentHashMap<>(1200);

    public TriColorLedLogService(DeviceRepository deviceRepository, TriColorLedLogRepository triColorLedLogRepository, TriColorLedMsgRepository triColorLedMsgRepository) {
        this.deviceRepository = deviceRepository;
        this.triColorLedLogRepository = triColorLedLogRepository;
        this.triColorLedMsgRepository = triColorLedMsgRepository;
    }

    @Transactional
    public void save(String message, LocalDateTime inTime) {
        String[] content = message.split("#");
        String serialNumber = content[0];
        String serialNumber1 = content[1];
        String signal = content[2];
        int voltage = Integer.parseInt(content[3]);

        deviceRepository.findDeviceBySerialNumberAndSerialNumber1AndType(serialNumber, serialNumber1, Device.DeviceType.TriColorLed).ifPresent(device -> {
            assert device.getId() != null;
            TriColorLedMsg msg = new TriColorLedMsg();
            msg.setMessage(message);
            msg.setTime(inTime);
            triColorLedMsgRepository.save(msg);

            TriColorLedLog triColorLedLog = new TriColorLedLog();
            triColorLedLog.setDevice(device);
            triColorLedLog.setVoltage(voltage);
            triColorLedLog.setDate(inTime.toLocalDate());
            triColorLedLog.setTime(inTime.toLocalTime());
            TriColorLedLog.Status status = TriColorLedLog.Status.NONE;
            if (signal.charAt(0) == '1') {
                status = TriColorLedLog.Status.RED;
            } else if (signal.charAt(1) == '1') {
                status = TriColorLedLog.Status.YELLOW;
            } else if (signal.charAt(2) == '1') {
                status = TriColorLedLog.Status.GREEN;
            }
            triColorLedLog.setStatus(status);
            Proxy proxy = LAST_LOG_CACHE.computeIfAbsent(device.getId(), key -> new Proxy());
            proxy.setLog(triColorLedLog, inTime, triColorLedLogRepository, interval);
        });
    }

    private final DeviceRepository deviceRepository;
    private final TriColorLedLogRepository triColorLedLogRepository;
    private final TriColorLedMsgRepository triColorLedMsgRepository;
    @Value("${system.tri-color-led-interval:3}")
    private int interval;

    private static class Proxy {
        synchronized void setLog(TriColorLedLog newLog
                , LocalDateTime inTime
                , TriColorLedLogRepository triColorLedLogRepository
                , int interval) {
            if (log == null) {
                log = triColorLedLogRepository.save(newLog);
            } else {
                LocalDateTime heartbeat = lastModifyTime.plusMinutes(2 * interval);
                if (inTime.isAfter(heartbeat) || newLog.getStatus() != log.getStatus()) {
                    Duration duration;
                    LocalDateTime startTime = log.getLogInTime();
                    if (inTime.isAfter(heartbeat)) {
                        duration = Duration.between(startTime, lastModifyTime.plusMinutes(interval));
                    } else {
                        duration = Duration.between(startTime, inTime);
                    }
                    log.setDuration(duration.abs().getSeconds());
                    log.setDevice(newLog.getDevice());
                    triColorLedLogRepository.save(log);
                    log = triColorLedLogRepository.save(newLog);
                }
            }
            lastModifyTime = inTime;
        }

        private LocalDateTime lastModifyTime;
        private TriColorLedLog log;
    }
}
