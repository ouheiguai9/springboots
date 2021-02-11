package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.device.Device;
import com.byakuya.boot.factory.component.device.log.TriColorLedLog;
import com.byakuya.boot.factory.component.device.log.TriColorLedLogService;
import com.byakuya.boot.factory.component.factory.machine.Machine;
import com.byakuya.boot.factory.component.factory.machine.MachineRepository;
import com.byakuya.boot.factory.component.factory.schedual.Schedule;
import com.byakuya.boot.factory.component.factory.schedual.ScheduleService;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.CustomizedException;
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
import java.util.*;
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
    public ResponseEntity<Map<TriColorLedLog.Status, Object>> rank(@AuthenticationPrincipal AuthenticationUser user
            , TimeType timeType
            , @RequestParam(required = false) LocalDateTime start
            , @RequestParam(required = false) LocalDateTime end
            , @RequestParam(required = false, defaultValue = "5") int top) {
        Pair<LocalDateTime, LocalDateTime> pair = compute(user.getUserId(), timeType, start, end);
        start = pair.getFirst();
        end = pair.getSecond();
        if (start == end) {
            return ResponseEntity.ok(null);
        }
        long totalSecond = Duration.between(start, end).abs().getSeconds();
        List<Machine> machines = machineRepository.findAllBindTriColorLED(user.getUserId());
        List<Device> devices = machines.stream().map(Machine::getTriColorLED).collect(Collectors.toList());
        List<TriColorLedLog> logList = triColorLedLogService.getDeviceStatusSumDuration(devices, start, end);
        Map<String, Machine> machineMap = machines.stream().collect(Collectors.toMap(m -> m.getTriColorLED().getId(), m -> m));
        List<MachineDuration> all = logList.stream().collect(Collectors.groupingBy(log -> log.getDevice().getId())).entrySet().stream().map(item -> {
            MachineDuration machineDuration = new MachineDuration(machineMap.remove(item.getKey()), totalSecond);
            item.getValue().forEach(machineDuration::addLog);

            //处理最后一次状态
            TriColorLedLogService.Proxy proxy = triColorLedLogService.getDeviceLastLog(machineDuration.getDeviceId());
            TriColorLedLog lastLog = new TriColorLedLog(null, proxy.getLog().getStatus(), 0);
            LocalDateTime durationEnd = pair.getSecond();
            if (triColorLedLogService.getHeartBeat(proxy.getLastModifyTime()).isBefore(durationEnd)) {
                durationEnd = triColorLedLogService.addInterval(proxy.getLastModifyTime());
            }
            lastLog.setDuration(Duration.between(proxy.getLog().getTime(), durationEnd).abs().getSeconds());
            return machineDuration.addLog(lastLog);
        }).collect(Collectors.toList());

        //处理从未上线的机器
        if (!machineMap.isEmpty()) {
            machines.forEach(m -> {
                if (machineMap.containsKey(m.getTriColorLEDId())) {
                    all.add(new MachineDuration(m, totalSecond));
                }
            });
        }

        Map<TriColorLedLog.Status, Object> rtnVal = new HashMap<>();
        rtnVal.put(TriColorLedLog.Status.RED, all.stream().sorted(Comparator.comparingLong(x -> -x.getRedDuration())).limit(top).collect(Collectors.toList()));
        rtnVal.put(TriColorLedLog.Status.YELLOW, all.stream().sorted(Comparator.comparingLong(x -> -x.getYellowDuration())).limit(top).collect(Collectors.toList()));
        rtnVal.put(TriColorLedLog.Status.GREEN, all.stream().sorted(Comparator.comparingLong(x -> -x.getGreenDuration())).limit(top).collect(Collectors.toList()));
        rtnVal.put(TriColorLedLog.Status.NONE, all.stream().sorted(Comparator.comparingLong(x -> -x.getNoneDuration())).limit(top).collect(Collectors.toList()));
        return ResponseEntity.ok(rtnVal);
    }

    /**
     * 计算时间段
     *
     * @param userId   当前用户
     * @param timeType 时间类型
     * @param start    开始时间
     * @param end      结束时间
     * @return 时间段
     */
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
                } else {
                    throw new CustomizedException("ERR-91001");
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
    public ResponseEntity<FactoryView> read(@AuthenticationPrincipal AuthenticationUser user) {
        List<Machine> machines = machineRepository.findAllBindTriColorLED(user.getUserId());
        int[] totalArr = new int[]{0, 0, 0, 0};
        LocalDateTime start = compute(user.getUserId(), TimeType.S, null, null).getFirst(), now = LocalDateTime.now();
        List<MachineStatus> list = machines.stream().map(machine -> {
            MachineStatus machineStatus = new MachineStatus();
            machineStatus.setName(machine.getName());
            machineStatus.setType(machine.getType());
            machineStatus.setOperator(machine.getOperator());
            machineStatus.setDeviceId(machine.getTriColorLEDId());
            machineStatus.setSerialNumber(machine.getTriColorLEDSerialNumber());
            machineStatus.setDetail(machine.getDescription());
            LocalDateTime durationStart;
            TriColorLedLog.Status status;
            TriColorLedLogService.Proxy proxy = triColorLedLogService.getDeviceLastLog(machine.getTriColorLEDId());
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

    @GetMapping("/singleView")
    public ResponseEntity<SingleView> read(@AuthenticationPrincipal AuthenticationUser user
            , String deviceId
            , TimeType timeType
            , @RequestParam(required = false) LocalDateTime start
            , @RequestParam(required = false) LocalDateTime end) {
        SingleView singleView = new SingleView();
        Pair<LocalDateTime, LocalDateTime> pair = compute(user.getUserId(), timeType, start, end);
        start = pair.getFirst();
        end = pair.getSecond();
        if (start == end) {
            return ResponseEntity.ok(null);
        }
        singleView.setTotalSecond(Duration.between(start, end).abs().getSeconds());
        List<TriColorLedLog> logList = triColorLedLogService.getAllLog(deviceId, start, end);
        long[] durations = new long[3];
        logList.forEach(log -> {
            singleView.addLog(log);
            if (log.getStatus() != TriColorLedLog.Status.NONE) {
                durations[log.getStatus().ordinal()] += log.getDuration();
                if (log.getStatus() == TriColorLedLog.Status.GREEN) {
                    singleView.addDuration(durations[TriColorLedLog.Status.RED.ordinal()], durations[TriColorLedLog.Status.YELLOW.ordinal()], durations[TriColorLedLog.Status.GREEN.ordinal()]);
                    Arrays.fill(durations, 0);
                }
            }
        });
        return ResponseEntity.ok(singleView);
    }

    private final MachineRepository machineRepository;
    private final ScheduleService scheduleService;
    private final TriColorLedLogService triColorLedLogService;
}
