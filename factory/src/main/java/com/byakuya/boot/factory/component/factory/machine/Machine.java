package com.byakuya.boot.factory.component.factory.machine;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.device.Device;
import com.byakuya.boot.factory.component.factory.workshop.Workshop;
import com.byakuya.boot.factory.component.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/29.
 */
@Setter
@Getter
@Entity
@Table(name = "T_APP_FACTORY_MACHINE")
@NamedEntityGraph(name = "Machine.List", attributeNodes = {@NamedAttributeNode("triColorLED"), @NamedAttributeNode("workshop")})
public class Machine extends AbstractAuditableEntity<User> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    @JsonIgnore
    public Device getTriColorLED() {
        return triColorLED;
    }

    @JsonProperty
    public void setTriColorLED(Device triColorLED) {
        this.triColorLED = triColorLED;
    }

    public String getTriColorLEDId() {
        return Optional.ofNullable(triColorLED).map(Device::getId).orElse(null);
    }

    public String getTriColorLEDSerialNumber() {
        return Optional.ofNullable(triColorLED).map(Device::getSerialNumber).orElse(null);
    }

    @JsonProperty
    public String getVirtualName() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(getUnknown())) {
            sb.append(getUnknown());
        } else {
            if (StringUtils.hasText(getBrand())) {
                sb.append(getBrand());
            }
            if (StringUtils.hasText(getType())) {
                sb.append(getType());
            }
        }
        if (StringUtils.hasText(getName())) {
            sb.append(getName());
        }
        return sb.toString();
    }

    @JsonIgnore
    public Workshop getWorkshop() {
        return workshop;
    }

    @JsonProperty
    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }

    public String getWorkshopId() {
        return Optional.ofNullable(workshop).map(Workshop::getId).orElse(null);
    }

    public String getWorkshopName() {
        return Optional.ofNullable(workshop).map(Workshop::getName).orElse(null);
    }
    private String brand;
    private String category;
    private String country;
    private String description;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotBlank
    @Column(nullable = false)
    private String operator;
    private String ordering;
    private String producer;
    private String software;
    @OneToOne
    @JoinColumn(name = "tri_color_led", unique = true)
    private Device triColorLED;
    private String type;
    private String unknown;
    @ManyToOne
    @JoinColumn(name = "workshop_id")
    private Workshop workshop;
}
