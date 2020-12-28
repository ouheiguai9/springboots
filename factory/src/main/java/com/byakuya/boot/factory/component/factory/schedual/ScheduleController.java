package com.byakuya.boot.factory.component.factory.schedual;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import com.byakuya.boot.factory.security.AuthenticationUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganzl on 2020/12/28.
 */
@AuthRestAPIController(path = {"factory/schedules"})
@Validated
public class ScheduleController {
    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping
    public ResponseEntity<Schedule> create(@Valid @RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleRepository.save(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> lockSchedule(@AuthenticationPrincipal AuthenticationUser user, @PathVariable String id) {
        scheduleRepository.delete(get(id, user));
        return ResponseEntity.ok(true);
    }

    private Schedule get(String id, AuthenticationUser user) {
        return scheduleRepository.findByIdAndCreatedBy_id(id, user.getUserId()).orElseThrow(() -> new RecordNotExistsException(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<Schedule>> read(@AuthenticationPrincipal AuthenticationUser user) {
        Iterable<Schedule> iterable = scheduleRepository.findAllByCreatedBy_idOrderByStartTimeAsc(user.getUserId());
        if (!iterable.iterator().hasNext()) {
            List<Schedule> defaults = new ArrayList<>();
            int size = 3, hour = 24 / size;
            LocalTime timeZero = LocalTime.of(0, 0, 0);
            for (int i = 1; i <= size; i++) {
                Schedule tmp = new Schedule();
                tmp.setName(String.format("第%d班", i));
                tmp.setStartTime(timeZero.plusHours(hour * (i - 1)));
                tmp.setEndTime(timeZero.plusHours(hour * i));
                defaults.add(tmp);
            }
            iterable = scheduleRepository.saveAll(defaults);
        }
        return ResponseEntity.ok(iterable);
    }

    @PutMapping
    public ResponseEntity<Schedule> update(@AuthenticationPrincipal AuthenticationUser user
            , @Valid @RequestBody Schedule schedule) {
        Schedule old = get(schedule.getId(), user);
        old.setName(schedule.getName());
        old.setStartTime(schedule.getStartTime());
        old.setEndTime(schedule.getEndTime());
        return ResponseEntity.ok(scheduleRepository.save(old));
    }

    private final ScheduleRepository scheduleRepository;
}
