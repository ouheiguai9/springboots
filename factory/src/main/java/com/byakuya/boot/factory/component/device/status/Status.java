package com.byakuya.boot.factory.component.device.status;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.device.Device;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by ganzl on 2021/1/4.
 */
@Data
public class Status implements Serializable {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
    private boolean green;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    private String original;
    private boolean red;
    private LocalTime time;
    private int voltage;
    private boolean yellow;
}
