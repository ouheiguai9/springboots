package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.device.log.TriColorLedLog;
import com.byakuya.boot.factory.component.factory.machine.Machine;
import lombok.Data;

/**
 * Created by ganzl on 2021/1/29.
 */
@Data
class MachineDuration {
    MachineDuration(Machine machine, long total) {
        this.deviceId = machine.getTriColorLEDId();
        this.name = machine.getName();
        this.serialNumber = machine.getTriColorLEDSerialNumber();
        this.noneDuration = total;
    }

    MachineDuration addLog(TriColorLedLog log) {
        noneDuration -= log.getDuration();
        switch (log.getStatus()) {
            case RED:
                redDuration += log.getDuration();
                break;
            case YELLOW:
                yellowDuration += log.getDuration();
                break;
            case GREEN:
                greenDuration += log.getDuration();
                break;
            default:
                noneDuration += log.getDuration();
        }
        return this;
    }

    private String deviceId;
    private long greenDuration = 0;
    private String name;
    private long noneDuration;
    private long redDuration = 0;
    private String serialNumber;
    private long yellowDuration = 0;
}
