package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by ganzl on 2020/12/22.
 */
@AuthRestAPIController(path = {"users"})
@Validated
public class SecurityUserController {
    public SecurityUserController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @PostMapping
    public ResponseEntity<SecurityUser> create(@Valid @RequestBody SecurityUser securityUser) {
        return ResponseEntity.ok(securityUserService.regist(securityUser));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SecurityUser> read(@PathVariable String id) {
        return ResponseEntity.ok(securityUserService.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<SecurityUser>> read(@PageableDefault Pageable pageable, String search) {
        return ResponseEntity.ok(securityUserService.queryList(pageable, search));
    }

    @PutMapping
    public ResponseEntity<SecurityUser> update(@Valid @RequestBody SecurityUser securityUser) {
        return ResponseEntity.ok(securityUserService.regist(securityUser));
    }

    private final SecurityUserService securityUserService;
}
