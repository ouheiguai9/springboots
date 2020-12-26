package com.byakuya.boot.factory.component.menu;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ganzl on 2020/12/22.
 */
@AuthRestAPIController(path = {"menus"})
@Validated
public class MenuController {

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @PostMapping
    public ResponseEntity<Menu> create(@Valid @RequestBody Menu menu) {
        if (StringUtils.hasText(menu.getParentId())) {
            menu.setParent(menuRepository.findById(menu.getParentId()).orElse(null));
        } else {
            menu.setParent(null);
        }
        return ResponseEntity.ok(menuRepository.save(menu));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Menu> read(@PathVariable String id) {
        return ResponseEntity.ok(get(id));
    }

    private Menu get(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id));
    }

    @GetMapping
    public ResponseEntity<List<Menu>> read() {
        return ResponseEntity.ok(menuRepository.findAll().stream().filter(Menu::noParent).collect(Collectors.toList()));
    }

    @PutMapping
    public ResponseEntity<Menu> update(@Valid @RequestBody Menu menu) {
        Menu old = get(menu.getId());
        old.setName(menu.getName());
        old.setIcon(menu.getIcon());
        old.setDescription(menu.getDescription());
        old.setOrdering(menu.getOrdering());
        if (StringUtils.hasText(menu.getParentId())) {
            old.setParent(menuRepository.findById(menu.getParentId()).orElse(null));
        } else {
            old.setParent(null);
        }
        return ResponseEntity.ok(menuRepository.save(old));
    }

    private final MenuRepository menuRepository;
}
