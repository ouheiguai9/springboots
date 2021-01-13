package com.byakuya.boot.factory.component.factory.schedual;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.security.AuthenticationUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by ganzl on 2020/12/28.
 */
@AuthRestAPIController(path = {"factory/schedules"})
@Validated
public class ScheduleController {
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<Schedule> create(@Valid @RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.create(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@AuthenticationPrincipal AuthenticationUser user, @PathVariable String id) {
        scheduleService.delete(user.getUserId(), id);
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<Iterable<Schedule>> read(@AuthenticationPrincipal AuthenticationUser user) {
        return ResponseEntity.ok(scheduleService.read(user.getUserId()));
    }

    @PutMapping
    public ResponseEntity<Schedule> update(@AuthenticationPrincipal AuthenticationUser user
            , @Valid @RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.update(user.getUserId(), schedule));
    }

    private final ScheduleService scheduleService;
}
