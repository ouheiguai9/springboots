package com.byakuya.boot.factory.component.factory.machine;

import com.byakuya.boot.factory.component.device.Device;
import com.byakuya.boot.factory.component.device.DeviceRepository;
import com.byakuya.boot.factory.component.factory.workshop.Workshop;
import com.byakuya.boot.factory.component.factory.workshop.WorkshopRepository;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import com.byakuya.boot.factory.jackson.DynamicJsonView;
import com.byakuya.boot.factory.security.AuthenticationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/29.
 */
@AuthRestAPIController(path = {"factory/machines"})
@Validated
public class MachineController {
    public MachineController(DeviceRepository deviceRepository, MachineRepository machineRepository, WorkshopRepository workshopRepository) {
        this.deviceRepository = deviceRepository;
        this.machineRepository = machineRepository;
        this.workshopRepository = workshopRepository;
    }

    @PostMapping
    public ResponseEntity<Machine> create(@AuthenticationPrincipal AuthenticationUser user
            , @Valid @RequestBody Machine machine) {
        setReferenceProperties(machine, user);
        return ResponseEntity.ok(machineRepository.save(machine));
    }

    private void setReferenceProperties(Machine machine, AuthenticationUser user) {
        machine.setTriColorLED(Optional.ofNullable(machine.getTriColorLED()).map(Device::getId).flatMap(id -> deviceRepository.findByIdAndConsumer_idAndType(id, user.getUserId(), Device.DeviceType.TriColorLed)).orElse(null));
        machine.setWorkshop(Optional.ofNullable(machine.getWorkshop()).map(Workshop::getId).flatMap(id -> workshopRepository.findByIdAndCreatedBy_id(id, user.getUserId())).orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@AuthenticationPrincipal AuthenticationUser user, @PathVariable String id) {
        machineRepository.delete(get(id, user));
        return ResponseEntity.ok(true);
    }

    private Machine get(String id, AuthenticationUser user) {
        return machineRepository.findByIdAndCreatedBy_id(id, user.getUserId()).orElseThrow(() -> new RecordNotExistsException(id));
    }

    @GetMapping("/triColorLED")
    @DynamicJsonView(include = {"id", "serialNumber"}, type = Device.class)
    public ResponseEntity<List<Device>> read(@AuthenticationPrincipal AuthenticationUser user) {
        return ResponseEntity.ok(deviceRepository.findAllByConsumer_idAndTypeAndLockedFalse(user.getUserId(), Device.DeviceType.TriColorLed));
    }

    @GetMapping
    public ResponseEntity<Page<Machine>> read(@AuthenticationPrincipal AuthenticationUser user
            , @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Machine> iterable = machineRepository.findAllByCreatedBy_id(pageable, user.getUserId());
        return ResponseEntity.ok(iterable);
    }

    @PutMapping
    public ResponseEntity<Machine> update(@AuthenticationPrincipal AuthenticationUser user
            , @Valid @RequestBody Machine machine) {
        Machine old = get(machine.getId(), user);
        old.setName(machine.getName());
        old.setProducer(machine.getProducer());
        old.setOperator(machine.getOperator());
        old.setDescription(machine.getDescription());
        old.setOrdering(machine.getOrdering());
        old.setCategory(machine.getCategory());
        old.setType(machine.getType());
        old.setCountry(machine.getCountry());
        old.setBrand(machine.getBrand());
        old.setUnknown(machine.getUnknown());

        setReferenceProperties(machine, user);
        old.setTriColorLED(machine.getTriColorLED());
        old.setWorkshop(machine.getWorkshop());
        return ResponseEntity.ok(machineRepository.save(old));
    }

    private final DeviceRepository deviceRepository;
    private final MachineRepository machineRepository;
    private final WorkshopRepository workshopRepository;
}
