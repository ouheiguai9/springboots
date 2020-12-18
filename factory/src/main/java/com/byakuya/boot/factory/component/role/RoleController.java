package com.byakuya.boot.factory.component.role;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by ganzl on 2020/12/18.
 */
@AuthRestAPIController(path = {"roles"})
public class RoleController {
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Role> getRole(@PathVariable String id) {
        return ResponseEntity.ok(roleRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id)));
    }

    private RoleRepository roleRepository;
}
