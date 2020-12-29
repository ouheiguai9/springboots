package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.component.UpdateGroup;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.jackson.DynamicJsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    @PostMapping(value = "/locked")
    public ResponseEntity<User> lockRole(@NotBlank String id, boolean locked) {
        return ResponseEntity.ok(userService.modifyLocked(id, locked));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> read(@PathVariable String id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<User>> read(@PageableDefault Pageable pageable, String search) {
        return ResponseEntity.ok(userService.queryList(pageable, search));
    }

    @GetMapping("/menu/{id}")
    public ResponseEntity<Iterable<String>> readRoleAllMenuId(@PathVariable String id) {
        return ResponseEntity.ok(userService.queryUserAllMenuId(id));
    }

    @PostMapping("/authorize")
    public ResponseEntity<User> readRoleAllMenuId(@NotBlank String id, String menuIdStr) {
        return ResponseEntity.ok(userService.authorize(id, menuIdStr));
    }

    @GetMapping(value = "/simple")
    @DynamicJsonView(include = {"id", "username", "phone", "nickname"}, type = User.class)
    public ResponseEntity<Page<User>> readSimple(@PageableDefault Pageable pageable, String search) {
        return ResponseEntity.ok(userService.queryListSimple(pageable, search));
    }

    @PutMapping
    @Validated({UpdateGroup.class})
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.modifyAll(user));
    }

    private final UserService userService;
}
