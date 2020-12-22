package com.byakuya.boot.factory.component.role;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Created by ganzl on 2020/12/18.
 */
@AuthRestAPIController(path = {"roles"})
@Validated
public class RoleController {
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Role> getRole(@PathVariable String id) {
        return ResponseEntity.ok(roleRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id)));
    }

    @GetMapping
    public ResponseEntity<Page<Role>> getRoleList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(roleRepository.findAll(pageable));
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

    @PutMapping
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) {
        Role old = get(role.getId());
        old.setName(role.getName());
        old.setDescription(role.getDescription());
        return ResponseEntity.ok(roleRepository.save(old));
    }

    private RoleRepository roleRepository;
}
