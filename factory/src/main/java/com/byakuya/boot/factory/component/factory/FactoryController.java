package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.device.log.TriColorLedLog;
import com.byakuya.boot.factory.component.device.log.TriColorLedLogService;
import com.byakuya.boot.factory.component.factory.machine.Machine;
import com.byakuya.boot.factory.component.factory.machine.MachineRepository;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.security.AuthenticationUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ganzl on 2021/1/6.
 */
@AuthRestAPIController(path = {"factory"})
@Validated
public class FactoryController {
    public FactoryController(MachineRepository machineRepository, TriColorLedLogService triColorLedLogService) {
        this.machineRepository = machineRepository;
        this.triColorLedLogService = triColorLedLogService;
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
    private final TriColorLedLogService triColorLedLogService;
}
