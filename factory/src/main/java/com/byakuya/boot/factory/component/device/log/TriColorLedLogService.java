package com.byakuya.boot.factory.component.device.log;

import com.byakuya.boot.factory.component.device.Device;
import com.byakuya.boot.factory.component.device.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ganzl on 2021/1/5.
 */
@Slf4j
@Service
public class TriColorLedLogService {
    private static final ConcurrentHashMap<String, Proxy> LAST_LOG_CACHE = new ConcurrentHashMap<>(1200);

    public TriColorLedLogService(DeviceRepository deviceRepository, TriColorLedLogRepository triColorLedLogRepository, TriColorLedMsgRepository triColorLedMsgRepository) {
        this.deviceRepository = deviceRepository;
        this.triColorLedLogRepository = triColorLedLogRepository;
        this.triColorLedMsgRepository = triColorLedMsgRepository;
    }

    public LocalDateTime addInterval(LocalDateTime time) {
        return time.plusMinutes(interval);
    }

    public String formatDuration(Duration duration) {
        Duration copy = Duration.from(duration);
        StringBuilder builder = new StringBuilder();
        long days = copy.toDays();
        if (days > 0) {
            builder.append(days).append("天");
            copy = copy.minusDays(days);
        }
        long hours = copy.toHours();
        if (hours > 0) {
            builder.append(hours).append("小时");
            copy = copy.minusHours(hours);
        }
        long minutes = copy.toMinutes();
        if (minutes > 0) {
            builder.append(minutes).append("分钟");
            copy = copy.minusMinutes(minutes);
        }
        builder.append(copy.getSeconds()).append("秒");
        return builder.toString();
    }

    public List<TriColorLedLog> getAllLog(String deviceId, LocalDateTime start, LocalDateTime end) {
        return triColorLedLogRepository.findAllByDevice_IdAndTimeBetweenOrderByTimeAsc(deviceId, start, end);
    }

    public List<TriColorLedLog> getDeviceGreenCount(Iterable<Device> devices, LocalDateTime start, LocalDateTime end) {
        return triColorLedLogRepository.findGreenCount(devices, start, end);
    }

    public List<TriColorLedLog> getDeviceStatusSumDuration(Iterable<Device> devices, LocalDateTime start, LocalDateTime end) {
        return triColorLedLogRepository.findStatusRank(devices, start, end);
    }

    public LocalDateTime getHeartBeat(LocalDateTime time) {
        return getHeartBeat(time, interval);
    }

    private static LocalDateTime getHeartBeat(LocalDateTime time, long interval) {
        return time.plusMinutes(2 * interval);
    }

    @Transactional
    public void save(String message, LocalDateTime inTime) {
        String[] content = message.split("#");
        String serialNumber = content[0];
        String serialNumber1 = content[1];
        String signal = content[2];
        int voltage = Integer.parseInt(content[3]);
        Optional<Device> opt = deviceRepository.findDeviceBySerialNumberAndSerialNumber1AndType(serialNumber, serialNumber1, Device.DeviceType.TriColorLed);
        if (opt.isPresent()) {
            Device device = opt.get();
            assert device.getId() != null;
            TriColorLedMsg msg = new TriColorLedMsg();
            msg.setMessage(message);
            msg.setTime(inTime);
            triColorLedMsgRepository.save(msg);

            TriColorLedLog triColorLedLog = new TriColorLedLog();
            triColorLedLog.setDevice(device);
            triColorLedLog.setVoltage(voltage);
            triColorLedLog.setTime(inTime);
            TriColorLedLog.Status status = TriColorLedLog.Status.NONE;
            if (signal.charAt(0) == '1') {
                status = TriColorLedLog.Status.RED;
            } else if (signal.charAt(1) == '1') {
                status = TriColorLedLog.Status.YELLOW;
            } else if (signal.charAt(2) == '1') {
                status = TriColorLedLog.Status.GREEN;
            }
            triColorLedLog.setStatus(status);
            Proxy proxy = getDeviceLastLog(device.getId());
            proxy.setLog(triColorLedLog, triColorLedLogRepository, interval);
        } else {
            log.warn("非法接入:\t{}", message);
        }
    }

    public Proxy getDeviceLastLog(String deviceId) {
        if (!StringUtils.hasText(deviceId)) {
            return new Proxy();
        }
        return LAST_LOG_CACHE.computeIfAbsent(deviceId, key -> {
            Proxy proxy = new Proxy();
            triColorLedLogRepository.findFirstByDevice_IdOrderByTimeDesc(key).ifPresent(x -> proxy.setLog(x, triColorLedLogRepository, interval));
            return proxy;
        });
    }

    private final DeviceRepository deviceRepository;
    private final TriColorLedLogRepository triColorLedLogRepository;
    private final TriColorLedMsgRepository triColorLedMsgRepository;
    @Value("${system.tri-color-led-interval:3}")
    private long interval;

    public static class Proxy {
        public LocalDateTime getLastModifyTime() {
            return lastModifyTime;
        }

        public synchronized TriColorLedLog getLog() {
            if (log == null) return null;
            TriColorLedLog copy = new TriColorLedLog();
            copy.setTime(log.getTime());
            copy.setStatus(log.getStatus());
            copy.setVoltage(log.getVoltage());
            return copy;
        }

        synchronized void setLog(TriColorLedLog newLog
                , TriColorLedLogRepository triColorLedLogRepository
                , long interval) {
            if (log == null) {
                log = triColorLedLogRepository.save(newLog);
            } else {
                LocalDateTime heartbeat = getHeartBeat(lastModifyTime, interval);
                if (newLog.getTime().isAfter(heartbeat) || newLog.getStatus() != log.getStatus()) {
                    Duration duration;
                    if (newLog.getTime().isAfter(heartbeat)) {
                        duration = Duration.between(log.getTime(), lastModifyTime.plusMinutes(interval));
                    } else {
                        duration = Duration.between(log.getTime(), newLog.getTime());
                    }
                    log.setDuration(duration.abs().getSeconds());
                    log.setDevice(newLog.getDevice());
                    triColorLedLogRepository.save(log);
                    log = triColorLedLogRepository.save(newLog);
                }
            }
            lastModifyTime = newLog.getTime();
        }

        private LocalDateTime lastModifyTime;
        private TriColorLedLog log;
    }
}
