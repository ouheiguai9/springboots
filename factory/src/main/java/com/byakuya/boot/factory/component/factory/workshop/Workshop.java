package com.byakuya.boot.factory.component.factory.workshop;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.factory.machine.Machine;
import com.byakuya.boot.factory.component.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Created by ganzl on 2021/4/26.
 */
@Setter
@Getter
@Entity
@Table(name = "T_APP_FACTORY_WORKSHOP")
public class Workshop extends AbstractAuditableEntity<User> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    private String description;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    @JsonIgnore
    @OneToMany(mappedBy = "workshop")
    private Set<Machine> machineSet;
    @NotBlank
    @Column(nullable = false)
    private String name;
}
