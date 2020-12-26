package com.byakuya.boot.factory.component.menu;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ganzl on 2020/12/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_MENU")
@NamedEntityGraph(name = "Menu.Graph", attributeNodes = {
        @NamedAttributeNode("parent")
        , @NamedAttributeNode(value = "children")
})
public class Menu extends AbstractAuditableEntity<User> {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    @JsonProperty
    public List<Menu> getOrderChildren() {
        if (orderChildren == null) {
            orderChildren = children.stream().sorted(Comparator.comparingInt(Menu::getOrdering).reversed()).collect(Collectors.toList());
        }
        return orderChildren;
    }

    public String getParentId() {
        if (StringUtils.hasText(parentId)) return parentId;
        return Optional.ofNullable(parent).map(Menu::getId).orElse(null);
    }

    public boolean noParent() {
        return parent == null;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
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
    @JsonIgnore
    @Transient
    private List<Menu> orderChildren;
    private int ordering;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu parent;
    @Transient
    private String parentId;
}
