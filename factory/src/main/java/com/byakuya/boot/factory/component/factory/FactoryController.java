package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.device.Device;
import com.byakuya.boot.factory.component.device.log.TriColorLedLog;
import com.byakuya.boot.factory.component.device.log.TriColorLedLogService;
import com.byakuya.boot.factory.component.factory.configuration.Configuration;
import com.byakuya.boot.factory.component.factory.configuration.ConfigurationRepository;
import com.byakuya.boot.factory.component.factory.machine.Machine;
import com.byakuya.boot.factory.component.factory.machine.MachineRepository;
import com.byakuya.boot.factory.component.factory.schedual.Schedule;
import com.byakuya.boot.factory.component.factory.schedual.ScheduleService;
import com.byakuya.boot.factory.component.factory.workshop.Workshop;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.CustomizedException;
import com.byakuya.boot.factory.security.AuthenticationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ganzl on 2021/1/6.
 */
@AuthRestAPIController(path = {"factory"})
@Validated
public class FactoryController {
    public FactoryController(ConfigurationRepository configurationRepository, MachineRepository machineRepository, ScheduleService scheduleService, TriColorLedLogService triColorLedLogService) {
        this.configurationRepository = configurationRepository;
        this.machineRepository = machineRepository;
        this.scheduleService = scheduleService;
        this.triColorLedLogService = triColorLedLogService;
    }

    @PostMapping("configuration")
    public ResponseEntity<Boolean> configuration(@AuthenticationPrincipal AuthenticationUser user, @RequestBody Configuration configuration) {
        Configuration old = configurationRepository.findByCreatedBy_idAndConfigurationType(user.getUserId(), configuration.getConfigurationType()).orElse(configuration);
        old.setContent(configuration.getContent());
        configurationRepository.save(old);
        return ResponseEntity.ok(true);
    }

    @GetMapping("configuration")
    public ResponseEntity<Configuration> configuration(@AuthenticationPrincipal AuthenticationUser user, Configuration.ConfigurationType configurationType) {
        return ResponseEntity.ok(configurationRepository.findByCreatedBy_idAndConfigurationType(user.getUserId(), configurationType).orElse(new Configuration()));
    }

    @GetMapping("/rank/conf")
    public ResponseEntity<List<RankConfView.GroupItem>> ranConf(@AuthenticationPrincipal AuthenticationUser user) {
        List<Machine> machines = machineRepository.findAllBindTriColorLED(user.getUserId());
        String conf = configurationRepository.findByCreatedBy_idAndConfigurationType(user.getUserId(), Configuration.ConfigurationType.EfficientRank).map(Configuration::getContent).orElse(null);
        return ResponseEntity.ok(machines.stream().collect(Collectors.groupingBy(machine -> Optional.ofNullable(machine.getWorkshop()).map(Workshop::getName).orElse(""))).entrySet().stream().map(item -> {
            RankConfView.GroupItem groupItem = new RankConfView.GroupItem();
            groupItem.setName(item.getKey());
            groupItem.setItems(item.getValue().stream().map(machine -> new RankConfView.ConfItem(machine, conf)).collect(Collectors.toList()));
            return groupItem;
        }).sorted().collect(Collectors.toList()));
    }

    @GetMapping("/rank")
    public ResponseEntity<Map<TriColorLedLog.Status, Object>> rank(@AuthenticationPrincipal AuthenticationUser user
            , TimeType timeType
            , @RequestParam(required = false) LocalDateTime start
            , @RequestParam(required = false) LocalDateTime end
            , @RequestParam(required = false, defaultValue = "10") int top) {
        Pair<LocalDateTime, LocalDateTime> pair = compute(user.getUserId(), timeType, start, end);
        start = pair.getFirst();
        end = pair.getSecond();
        if (start == end) {
            return ResponseEntity.ok(null);
        }
        long totalSecond = Duration.between(start, end).abs().getSeconds();
        List<Machine> machines = machineRepository.findAllBindTriColorLED(user.getUserId());

        configurationRepository.findByCreatedBy_idAndConfigurationType(user.getUserId(), Configuration.ConfigurationType.EfficientRank).ifPresent(configuration -> {
            if (StringUtils.hasText(configuration.getContent())) {
                List<Machine> copy = new ArrayList<>(machines);
                machines.clear();
                copy.forEach(item -> {
                    assert item.getId() != null;
                    if (configuration.getContent().contains(item.getId())) {
                        machines.add(item);
                    }
                });
            }
        });

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
    public ResponseEntity<FactoryView> read(@AuthenticationPrincipal AuthenticationUser user
            , @RequestParam(required = false, defaultValue = "0") int offset
            , @RequestParam(required = false, defaultValue = "10") int limit
            , @RequestParam(required = false) String workshopId) {
        List<Machine> machines = machineRepository.findAllBindTriColorLED(user.getUserId());
        if (StringUtils.hasText(workshopId)) {
            machines = machines.stream().filter(machine -> workshopId.equals(machine.getWorkshopId())).collect(Collectors.toList());
        }
        int[] totalArr = new int[]{0, 0, 0, 0};
        LocalDateTime start = compute(user.getUserId(), TimeType.S, null, null).getFirst(), now = LocalDateTime.now();

        Map<Device, Long> deviceLongMap = triColorLedLogService.getDeviceGreenCount(machines.stream().skip(offset).limit(limit).map(Machine::getTriColorLED).collect(Collectors.toList()), start, now).stream().collect(Collectors.toMap(TriColorLedLog::getDevice, TriColorLedLog::getDuration));

//        deviceLongMap.keySet().forEach(device -> {
//            triColorLedLogService.getLastLogBefore(device.getId(), start).ifPresent(log -> {
//                if (log.getStatus() == TriColorLedLog.Status.GREEN) {
//                    long duration = Duration.between(log.getTime(), start).abs().getSeconds();
//                    deviceLongMap.replace(device, deviceLongMap.get(device) + 1);
//                }
//            });
//        });

        List<MachineStatus> list = machines.stream().map(machine -> {
            MachineStatus machineStatus = new MachineStatus();
            machineStatus.setName(machine.getVirtualName());
            machineStatus.setOrdering(machine.getOrdering());
            machineStatus.setOperator(machine.getOperator());
            machineStatus.setDeviceId(machine.getTriColorLEDId());
            machineStatus.setSerialNumber(machine.getTriColorLEDSerialNumber());
            machineStatus.setDetail(machine.getDescription());
            machineStatus.setCount(deviceLongMap.getOrDefault(machine.getTriColorLED(), 0L));
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
        }).collect(Collectors.toList()).stream().skip(offset).limit(limit).collect(Collectors.toList());
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
        triColorLedLogService.getLastLogBefore(deviceId, start).ifPresent(log -> {
            long duration = Duration.between(log.getTime(), pair.getFirst()).abs().getSeconds();
            if (duration < log.getDuration()) {
                TriColorLedLog tmp = log.copy();
                tmp.setTime(pair.getFirst());
                tmp.setDuration(log.getDuration() - duration);
                logList.add(0, tmp);
            }
        });
        long[] durations = new long[3];
        for (int i = 0; i < logList.size(); i++) {
            TriColorLedLog log = logList.get(i);
            if (i == (logList.size() - 1)) {
                long duration = Duration.between(log.getTime(), end).abs().getSeconds();
                if (log.getDuration() == 0 || duration < log.getDuration()) {
                    log = log.copy();
                    log.setDuration(duration);
                }
            }
            singleView.addLog(log);
            if (log.getStatus() != TriColorLedLog.Status.NONE) {
                durations[log.getStatus().ordinal()] += log.getDuration();
                if (log.getStatus() == TriColorLedLog.Status.GREEN) {
                    singleView.addDuration(durations[TriColorLedLog.Status.RED.ordinal()], durations[TriColorLedLog.Status.YELLOW.ordinal()], durations[TriColorLedLog.Status.GREEN.ordinal()]);
                    Arrays.fill(durations, 0);
                }
            }
        }
        return ResponseEntity.ok(singleView);
    }

    @GetMapping("/abnormal")
    public ResponseEntity<List<MachineStatus>> read(@AuthenticationPrincipal AuthenticationUser user
            , LocalDateTime start
            , LocalDateTime end
            , TriColorLedLog.Status status
            , Long threshold) {
        List<Machine> machines = machineRepository.findAllBindTriColorLED(user.getUserId());
        Map<Device, Long> deviceLongMap = triColorLedLogService.getDeviceStatusOvertimeCount(machines.stream().map(Machine::getTriColorLED).collect(Collectors.toList()), status, start, end, threshold).stream().collect(Collectors.toMap(TriColorLedLog::getDevice, TriColorLedLog::getDuration));
        return ResponseEntity.ok(machines.stream().map(machine -> {
            MachineStatus machineStatus = new MachineStatus();
            machineStatus.setName(machine.getVirtualName());
            machineStatus.setOrdering(machine.getOrdering());
            machineStatus.setDeviceId(machine.getTriColorLEDId());
            machineStatus.setDetail(machine.getDescription());
            machineStatus.setCount(deviceLongMap.getOrDefault(machine.getTriColorLED(), 0L));
            machineStatus.setOperator(machine.getOperator());
            machineStatus.setStatus(status.getName());
            return machineStatus;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/abnormal/detail")
    public ResponseEntity<Page<TriColorLedLog>> read(@AuthenticationPrincipal AuthenticationUser user
            , @PageableDefault(sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable
            , String deviceId
            , LocalDateTime start
            , LocalDateTime end
            , TriColorLedLog.Status status
            , Long threshold) {
        if (StringUtils.hasText(deviceId)) {
            return ResponseEntity.ok(triColorLedLogService.getStatusOvertimeDetail(pageable, deviceId, start, end, status, threshold));
        } else {
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>()));
        }
    }

    private final ConfigurationRepository configurationRepository;
    private final MachineRepository machineRepository;
    private final ScheduleService scheduleService;
    private final TriColorLedLogService triColorLedLogService;
}
