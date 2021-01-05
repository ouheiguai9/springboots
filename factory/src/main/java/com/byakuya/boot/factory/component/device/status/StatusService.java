package com.byakuya.boot.factory.component.device.status;

import com.byakuya.boot.factory.component.device.DeviceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by ganzl on 2021/1/5.
 */
@Service
public class StatusService {
    public StatusService(DeviceRepository deviceRepository, StatusRepository statusRepository) {
        this.deviceRepository = deviceRepository;
        this.statusRepository = statusRepository;
    }

    public void save(String message, LocalDateTime inTime) {
        String[] content = message.split("#");
        String serialNumber = content[0];
        String serialNumber1 = content[1];
        String signal = content[2];
        int voltage = Integer.parseInt(content[3]);
        deviceRepository.findDeviceBySerialNumberAndSerialNumber1(serialNumber, serialNumber1).ifPresent(device -> {
            Status status = new Status();
            status.setOriginal(message);
            status.setDevice(device);
            status.setVoltage(voltage);
            status.setDate(inTime.toLocalDate());
            status.setTime(inTime.toLocalTime());
            status.setRed(signal.charAt(0) == '1');
            status.setYellow(signal.charAt(1) == '1');
            status.setGreen(signal.charAt(2) == '1');
            statusRepository.save(status);
        });
    }

    private final DeviceRepository deviceRepository;
    private final StatusRepository statusRepository;
}
