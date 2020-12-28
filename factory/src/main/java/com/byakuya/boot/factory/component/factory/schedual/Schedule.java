package com.byakuya.boot.factory.component.factory.schedual;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * Created by ganzl on 2020/12/28.
 */
@Setter
@Getter
@Entity
@Table(name = "T_APP_FACTORY_SCHEDULE")
public class Schedule extends AbstractAuditableEntity<User> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    @NotNull
    @Column(nullable = false)
    private LocalTime endTime;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotNull
    @Column(nullable = false)
    private LocalTime startTime;
}
