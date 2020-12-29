package com.byakuya.boot.factory.component.device;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/26.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_DEVICE")
@NamedEntityGraph(name = "Device.List", attributeNodes = {@NamedAttributeNode("consumer")})
public class Device extends AbstractAuditableEntity<User> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public String getConsumerId() {
        return Optional.ofNullable(consumer).map(User::getId).orElse(null);
    }

    public String getConsumerName() {
        return Optional.ofNullable(consumer).map(User::getNickname).orElse(null);
    }

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private User consumer;
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
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    public enum DeviceType {
        TriColorLed("三色灯"), RemoteUSB("远程硬盘");

        DeviceType(String name) {
            this.name = name;
        }

        @Getter
        private String name;
    }
}
