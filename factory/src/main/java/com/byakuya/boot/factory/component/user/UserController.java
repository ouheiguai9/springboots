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
public class UserController {
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.regist(user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> read(@PathVariable String id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<User>> read(@PageableDefault Pageable pageable, String search) {
        return ResponseEntity.ok(userService.queryList(pageable, search));
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.regist(user));
    }

    private final UserService userService;
}
