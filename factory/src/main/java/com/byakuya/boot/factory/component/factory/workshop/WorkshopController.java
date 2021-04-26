package com.byakuya.boot.factory.component.factory.workshop;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.CustomizedException;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import com.byakuya.boot.factory.security.AuthenticationUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by ganzl on 2021/4/26.
 */
@AuthRestAPIController(path = {"factory/workshops"})
@Validated
public class WorkshopController {
    public WorkshopController(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @PostMapping
    public ResponseEntity<Workshop> create(@Valid @RequestBody Workshop workshop) {
        return ResponseEntity.ok(workshopRepository.save(workshop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@AuthenticationPrincipal AuthenticationUser user, @PathVariable String id) {
        Workshop workshop = get(id, user);
        if (workshop.getMachineSet().isEmpty()) {
            workshopRepository.delete(get(id, user));
        } else {
            throw new CustomizedException("ERR-20005");
        }
        return ResponseEntity.ok(true);
    }

    private Workshop get(String id, AuthenticationUser user) {
        return workshopRepository.findByIdAndCreatedBy_id(id, user.getUserId()).orElseThrow(() -> new RecordNotExistsException(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<Workshop>> read(@AuthenticationPrincipal AuthenticationUser user) {
        return ResponseEntity.ok(workshopRepository.findAllByCreatedBy_idOrderByNameAsc(user.getUserId()));
    }

    @PutMapping
    public ResponseEntity<Workshop> update(@AuthenticationPrincipal AuthenticationUser user
            , @Valid @RequestBody Workshop workshop) {
        Workshop old = get(workshop.getId(), user);
        old.setName(workshop.getName());
        old.setDescription(workshop.getDescription());
        return ResponseEntity.ok(workshopRepository.save(old));
    }
    private final WorkshopRepository workshopRepository;
}
