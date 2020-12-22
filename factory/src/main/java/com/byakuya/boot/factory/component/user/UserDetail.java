package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.menu.Menu;
import com.byakuya.boot.factory.component.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by ganzl on 2020/11/30.
 */
@Data
@Embeddable
public class UserDetail implements Serializable {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public Set<Menu> getMenus() {
        Set<Menu> rtnVal = new HashSet<>(getMenuSet());
        Optional.ofNullable(roleSet).ifPresent(roles -> {
            roles.forEach(role -> {
                if (!role.isLocked()) rtnVal.addAll(role.getMenuSet());
            });
        });
        return rtnVal;
    }

    private String address;
    private String avatar;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "T_SYS_USER_MENU",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id")})
    private Set<Menu> menuSet;
    @NotBlank
    @Column(nullable = false)
    private String nickname;
    @ManyToMany
    @JoinTable(name = "T_SYS_USER_ROLE",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roleSet;
    private boolean sex;
}
