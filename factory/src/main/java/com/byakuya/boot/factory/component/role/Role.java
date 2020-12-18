package com.byakuya.boot.factory.component.role;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.menu.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Created by ganzl on 2020/12/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_ROLE")
public class Role extends AbstractAuditableEntity<Role> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    private String description;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "T_SYS_ROLE_MENU",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id")})
    private Set<Menu> menuSet;
    @NotBlank
    @Column(nullable = false)
    private String name;

}
