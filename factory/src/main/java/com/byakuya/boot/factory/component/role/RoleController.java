package com.byakuya.boot.factory.component.role;

import com.byakuya.boot.factory.component.menu.Menu;
import com.byakuya.boot.factory.component.menu.MenuRepository;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ganzl on 2020/12/18.
 */
@AuthRestAPIController(path = {"roles"})
@Validated
public class RoleController {
    public RoleController(MenuRepository menuRepository, RoleRepository roleRepository) {
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @PostMapping(value = "/locked")
    public ResponseEntity<Role> lockRole(@NotBlank String id, boolean locked) {
        Role old = get(id);
        old.setLocked(locked);
        return ResponseEntity.ok(roleRepository.save(old));
    }

    private Role get(String id) {
        return roleRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id));
    }

    @GetMapping
    public ResponseEntity<Page<Role>> read(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(roleRepository.findAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Role>> read() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Role> read(@PathVariable String id) {
        return ResponseEntity.ok(get(id));
    }

    @GetMapping("/menu/{id}")
    public ResponseEntity<Iterable<String>> readRoleAllMenuId(@PathVariable String id) {
        return ResponseEntity.ok(roleRepository.findRoleAllMenuId(id));
    }

    @PostMapping("/authorize")
    public ResponseEntity<Role> readRoleAllMenuId(@NotBlank String id, String menuIdStr) {
        Role old = get(id);
        Set<Menu> menuSet = new HashSet<>();
        if (StringUtils.hasText(menuIdStr)) {
            menuRepository.findAllById(Arrays.asList(menuIdStr.split(","))).forEach(menuSet::add);
        }
        old.setMenuSet(menuSet);
        return ResponseEntity.ok(roleRepository.save(old));
    }

    @PutMapping
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) {
        Role old = get(role.getId());
        old.setName(role.getName());
        old.setDescription(role.getDescription());
        return ResponseEntity.ok(roleRepository.save(old));
    }

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;
}
