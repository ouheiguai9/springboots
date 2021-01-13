package com.byakuya.boot.factory.component.factory.schedual;

import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ganzl on 2021/1/13.
 */
@Service
public class ScheduleService {
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule create(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void delete(String userId, String id) {
        scheduleRepository.delete(get(id, userId));
    }

    private Schedule get(String id, String userId) {
        return scheduleRepository.findByIdAndCreatedBy_id(id, userId).orElseThrow(() -> new RecordNotExistsException(id));
    }

    public Optional<Schedule> getCurrentSchedule(String userId) {
        List<Schedule> scheduleList = read(userId);
        LocalTime now = LocalTime.now();
        return scheduleList.stream().filter(schedule -> {
            if (schedule.getEndTime().isBefore(schedule.getStartTime())) {
                return now.isBefore(schedule.getEndTime()) || now.isAfter(schedule.getStartTime());
            } else {
                return now.isAfter(schedule.getStartTime()) && now.isBefore(schedule.getEndTime());
            }
        }).findFirst();
    }

    public List<Schedule> read(String userId) {
        List<Schedule> scheduleList = scheduleRepository.findAllByCreatedBy_idOrderByStartTimeAsc(userId);
        if (scheduleList.isEmpty()) {
            scheduleList = createDefault();
        }
        return scheduleList;
    }

    private List<Schedule> createDefault() {
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
        scheduleRepository.saveAll(defaults);
        return defaults;
    }

    public Schedule update(String userId, Schedule schedule) {
        Schedule old = get(schedule.getId(), userId);
        old.setName(schedule.getName());
        old.setStartTime(schedule.getStartTime());
        old.setEndTime(schedule.getEndTime());
        return scheduleRepository.save(old);
    }

    private final ScheduleRepository scheduleRepository;
}
