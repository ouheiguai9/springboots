package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.InsertGroup;
import com.byakuya.boot.factory.component.UpdateGroup;
import com.byakuya.boot.factory.component.menu.Menu;
import com.byakuya.boot.factory.component.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ganzl on 2020/11/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_USER")
@NamedEntityGraph(name = "User.Login"
        , attributeNodes = {
        @NamedAttributeNode(value = "roleSet", subgraph = "roleMenuSet")
        , @NamedAttributeNode(value = "menuSet", subgraph = "menuParent")}
        , subgraphs = {
        @NamedSubgraph(name = "roleMenuSet", attributeNodes = @NamedAttributeNode(value = "menuSet", subgraph = "menuParent"))
        , @NamedSubgraph(name = "menuParent", attributeNodes = @NamedAttributeNode("parent"))
})
@NamedEntityGraph(name = "User.List", attributeNodes = {@NamedAttributeNode("roleSet")})
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

    @SuppressWarnings("WeakerAccess")
    public String getRoleIdStr() {
        if (StringUtils.hasText(roleIdStr)) return roleIdStr;
        return Optional.ofNullable(roleSet).map(x -> x.stream().map(Role::getId).collect(Collectors.joining(","))).orElse(null);
    }

    public String getValidPeriod() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");
        String endStr = Optional.ofNullable(endValidPeriod).map(formatter::format).orElse("永久");
        return String.format("%s 至 %s", formatter.format(getBeginValidPeriod()), endStr);
    }

    public LocalDateTime getBeginValidPeriod() {
        return Optional.ofNullable(beginValidPeriod).orElse(LocalDateTime.now());
    }

    private String address;
    private String avatar;
    private LocalDateTime beginValidPeriod = LocalDateTime.now();
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
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class})
    @Column(nullable = false)
    private String nickname;
    @NotBlank(groups = {InsertGroup.class})
    private String password;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^1[0-9]{10}$", groups = {InsertGroup.class, UpdateGroup.class})
    @Column(unique = true, nullable = false)
    private String phone;
    @Transient
    private String roleIdStr;
    @ManyToMany
    @JoinTable(name = "T_SYS_USER_ROLE",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roleSet;
    private boolean sex;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class})
    @Column(unique = true, nullable = false)
    private String username;
}
