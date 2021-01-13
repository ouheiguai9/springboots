package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.device.Device;
import com.byakuya.boot.factory.component.device.log.TriColorLedLog;
import com.byakuya.boot.factory.component.device.log.TriColorLedLogService;
import com.byakuya.boot.factory.component.factory.machine.Machine;
import com.byakuya.boot.factory.component.factory.machine.MachineRepository;
import com.byakuya.boot.factory.component.factory.schedual.Schedule;
import com.byakuya.boot.factory.component.factory.schedual.ScheduleService;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.security.AuthenticationUser;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ganzl on 2021/1/6.
 */
@AuthRestAPIController(path = {"factory"})
@Validated
public class FactoryController {
    public FactoryController(MachineRepository machineRepository, ScheduleService scheduleService, TriColorLedLogService triColorLedLogService) {
        this.machineRepository = machineRepository;
        this.scheduleService = scheduleService;
        this.triColorLedLogService = triColorLedLogService;
    }

    @GetMapping("/rank")
    public ResponseEntity<List<TriColorLedLog>> rank(@AuthenticationPrincipal AuthenticationUser user
            , TimeType timeType
            , @RequestParam(required = false) LocalDateTime start
            , @RequestParam(required = false) LocalDateTime end) {
        Pair<LocalDateTime, LocalDateTime> pair = compute(user.getUserId(), timeType, start, end);
        if (pair.getFirst() == pair.getSecond()) {
            return ResponseEntity.ok(null);
        }
        long totalSecond = Duration.between(pair.getFirst(), pair.getSecond()).abs().getSeconds();
        List<Machine> machines = machineRepository.findAllByCreatedBy_idOrderByCreatedDateAsc(user.getUserId());
        List<Device> devices = machines.stream().map(Machine::getTriColorLED).collect(Collectors.toList());
        triColorLedLogService.getDeviceStatusSumDuration(devices, pair.getFirst(), pair.getSecond());
        return ResponseEntity.ok(triColorLedLogService.getDeviceStatusSumDuration(devices, pair.getFirst(), pair.getSecond()));
    }

    private Pair<LocalDateTime, LocalDateTime> compute(String userId, TimeType timeType, LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now(), pairStart = now, pairEnd = now;
        switch (timeType) {
            case S:
                Optional<Schedule> schedule = scheduleService.getCurrentSchedule(userId);
                if (schedule.isPresent()) {
                    if (now.toLocalTime().isBefore(schedule.get().getStartTime())) {
                        pairStart = LocalDateTime.of(now.minusDays(1).toLocalDate(), schedule.get().getStartTime());
                    } else {
                        pairStart = LocalDateTime.of(now.toLocalDate(), schedule.get().getStartTime());
                    }
                }
                break;
            case D:
                pairStart = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
                break;
            case D3:
                pairStart = now.minusDays(3);
                break;
            case W:
                pairStart = now.minusWeeks(1);
                break;
            case M:
                pairStart = now.minusMonths(1);
                break;
            case Y:
                pairStart = now.minusYears(1);
                break;
//            case W:
//                pairStart = LocalDateTime.of(now.minusDays(now.getDayOfWeek().getValue() - 1).toLocalDate(), LocalTime.MIN);
//                break;
//            case M:
//                pairStart = LocalDateTime.of(now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalTime.MIN);
//                break;
//            case Y:
//                pairStart = LocalDateTime.of(now.with(TemporalAdjusters.firstDayOfYear()).toLocalDate(), LocalTime.MIN);
//                break;
            default:
                if (start != null && end != null) {
                    pairStart = start;
                    pairEnd = end;
                }
        }
        return Pair.of(pairStart, pairEnd);
    }

    @GetMapping("/view")
    public ResponseEntity<FactoryView> read(@AuthenticationPrincipal AuthenticationUser user, long initTime) {
        List<Machine> machines = machineRepository.findAllByCreatedBy_idOrderByCreatedDateAsc(user.getUserId());
        int[] totalArr = new int[]{0, 0, 0, 0};
        LocalDateTime start = LocalDateTime.ofInstant(new Date(initTime).toInstant(), ZoneId.systemDefault()), now = LocalDateTime.now();
        List<MachineStatus> list = machines.stream().map(machine -> {
            MachineStatus machineStatus = new MachineStatus();
            machineStatus.setName(machine.getName());
            machineStatus.setType(machine.getType());
            machineStatus.setOperator(machine.getOperator());
            machineStatus.setDeviceId(machine.getTriColorLEDId());
            machineStatus.setSerialNumber(machine.getTriColorLEDSerialNumber());
            TriColorLedLogService.Proxy proxy = triColorLedLogService.getDeviceLastLog(machine.getTriColorLEDId());
            LocalDateTime durationStart;
            TriColorLedLog.Status status;
            if (proxy.getLastModifyTime() == null || triColorLedLogService.getHeartBeat(proxy.getLastModifyTime()).isBefore(now)) {
                status = TriColorLedLog.Status.NONE;
                if (proxy.getLastModifyTime() == null) {
                    durationStart = start;
                } else {
                    durationStart = triColorLedLogService.addInterval(proxy.getLastModifyTime());
                }
            } else {
                TriColorLedLog log = proxy.getLog();
                status = log.getStatus();
                durationStart = log.getTime();
            }
            machineStatus.setDuration(triColorLedLogService.formatDuration(Duration.between(durationStart, now).abs()));
            machineStatus.setStatus(status.getName());
            totalArr[status.ordinal()] += 1;
            return machineStatus;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(new FactoryView(start, now, list, totalArr));
    }

    private final MachineRepository machineRepository;
    private final ScheduleService scheduleService;
    private final TriColorLedLogService triColorLedLogService;
}
