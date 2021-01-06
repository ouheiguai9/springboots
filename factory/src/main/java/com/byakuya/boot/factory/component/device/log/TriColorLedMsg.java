package com.byakuya.boot.factory.component.device.log;

import com.byakuya.boot.factory.SystemVersion;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by ganzl on 2021/1/6.
 */
@Data
@Table(name = "T_SYS_DEVICE_LED_MSG")
class TriColorLedMsg implements Serializable {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    private String message;
    private LocalDateTime time;
}
