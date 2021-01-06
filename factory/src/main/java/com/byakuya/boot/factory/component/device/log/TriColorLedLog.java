package com.byakuya.boot.factory.component.device.log;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.device.Device;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by ganzl on 2021/1/4.
 */
@Data
@Table(name = "T_SYS_DEVICE_LED_LOG")
public class TriColorLedLog implements Serializable {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public LocalDateTime getLogInTime() {
        return LocalDateTime.of(date, time);
    }
    private LocalDate date;
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
    private LocalTime time;
    private int voltage;

    enum Status {
        RED, YELLOW, GREEN, NONE
    }
}
