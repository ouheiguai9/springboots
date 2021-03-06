package com.byakuya.boot.factory.component.device.log;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.device.Device;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by ganzl on 2021/1/4.
 */
@Data
@Entity
@Table(name = "T_SYS_DEVICE_LED_LOG")
public class TriColorLedLog implements Serializable {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    TriColorLedLog() {

    }

    public TriColorLedLog(Device device, Status status, long duration) {
        this.device = device;
        this.status = status;
        this.duration = duration;
    }

    public TriColorLedLog copy() {
        TriColorLedLog rtnVal = new TriColorLedLog(this.device, this.status, this.duration);
        rtnVal.setId(this.id);
        rtnVal.setTime(this.time);
        rtnVal.setVoltage(this.voltage);
        return rtnVal;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;
    private long duration;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime time;
    private int voltage;

    public enum Status {
        RED("故障"), YELLOW("暂停"), GREEN("运行"), NONE("离线");

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private String name;
    }
}
