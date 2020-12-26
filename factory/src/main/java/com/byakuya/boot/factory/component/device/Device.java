package com.byakuya.boot.factory.component.device;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Created by ganzl on 2020/12/26.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_DEVICE")
public class Device extends AbstractAuditableEntity<User> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    private String consumer;
    private String description;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    private String producer;
    @NotBlank
    @Column(nullable = false)
    private String serialNumber;
    private String serialNumber1;
    private String serialNumber2;
    @NotBlank
    @Column(nullable = false)
    private String type;
}
