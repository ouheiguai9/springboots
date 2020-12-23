package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.menu.Menu;
import com.byakuya.boot.factory.component.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by ganzl on 2020/11/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_USER")
@NamedEntityGraph(name = "User.Graph", attributeNodes = {@NamedAttributeNode("roleSet"), @NamedAttributeNode("menuSet")})
public class User extends AbstractAuditableEntity<User> {

    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    private String address;
    private String avatar;
    private LocalDateTime beginValidPeriod = LocalDateTime.now();
    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;
    private LocalDateTime endValidPeriod;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    private LocalDateTime lastPasswordModifiedDate;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "T_SYS_USER_MENU",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id")})
    private Set<Menu> menuSet;
    @NotBlank
    @Column(nullable = false)
    private String nickname;
    @NotBlank
    private String password;
    @NotBlank
    @Pattern(regexp = "^1[0-9]{10}$")
    @Column(unique = true, nullable = false)
    private String phone;
    @ManyToMany
    @JoinTable(name = "T_SYS_USER_ROLE",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roleSet;
    private boolean sex;
    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;
}
