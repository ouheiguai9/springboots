package com.byakuya.boot.factory.component.menu;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

/**
 * Created by ganzl on 2020/12/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_MENU")
@NamedEntityGraph(name = "Menu.Graph", attributeNodes = {@NamedAttributeNode("parent"), @NamedAttributeNode("children")})
public class Menu extends AbstractAuditableEntity<Menu> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public Optional<String> getParentId() {
        if (StringUtils.hasText(parentId)) return Optional.of(parentId);
        return Optional.ofNullable(parent).map(Menu::getId);
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    private Set<Menu> children;
    @NotBlank
    @Column(unique = true, updatable = false, nullable = false)
    private String code;
    private String description;
    private String icon;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    private int ordering;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu parent;
    @Transient
    private String parentId;
}
