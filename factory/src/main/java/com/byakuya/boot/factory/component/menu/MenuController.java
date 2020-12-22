package com.byakuya.boot.factory.component.menu;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Stream;

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
        menu.getParentId().ifPresent(x -> menu.setParent(get(x)));
        return ResponseEntity.ok(menuRepository.save(menu));
    }

    private Menu get(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Menu> read(@PathVariable String id) {
        return ResponseEntity.ok(get(id));
    }

    @GetMapping
    public ResponseEntity<Stream<Menu>> read() {
        return ResponseEntity.ok(menuRepository.findAll().stream().filter(x -> x.getParent() == null));
    }

    @PutMapping
    public ResponseEntity<Menu> update(@Valid @RequestBody Menu menu) {
        Menu old = get(menu.getId());
        old.setName(menu.getName());
        old.setIcon(menu.getIcon());
        old.setDescription(menu.getDescription());
        old.setOrdering(menu.getOrdering());
        if (!old.getParentId().equals(menu.getParentId())) {
            old.setParent(menu.getParentId().map(this::get).orElse(null));
        }
        return ResponseEntity.ok(menuRepository.save(old));
    }

    private final MenuRepository menuRepository;
}
