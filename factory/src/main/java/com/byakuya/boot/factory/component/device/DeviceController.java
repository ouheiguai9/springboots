package com.byakuya.boot.factory.component.device;

import com.byakuya.boot.factory.component.factory.machine.MachineRepository;
import com.byakuya.boot.factory.component.user.User;
import com.byakuya.boot.factory.component.user.UserRepository;
import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import com.byakuya.boot.factory.jackson.DynamicJsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/26.
 */
@AuthRestAPIController(path = {"devices"})
@Validated
public class DeviceController {
    public DeviceController(DeviceRepository deviceRepository, MachineRepository machineRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Device> create(@Valid @RequestBody Device device) {
        setReferenceProperties(device);
        return ResponseEntity.ok(deviceRepository.save(device));
    }

    private void setReferenceProperties(Device device) {
        device.setConsumer(Optional.ofNullable(device.getConsumer()).map(User::getId).flatMap(userRepository::findById).orElse(null));
    }

    @PostMapping(value = "/locked")
    public ResponseEntity<Device> lockDevice(@NotBlank String id, boolean locked) {
        Device old = get(id);
        old.setLocked(locked);
        return ResponseEntity.ok(deviceRepository.save(old));
    }

    private Device get(String id) {
        return deviceRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id));
    }

    @GetMapping
    @DynamicJsonView(exclude = {"consumer"}, type = Device.class)
    public ResponseEntity<Page<Device>> read(@PageableDefault Pageable pageable, String search) {
        if (StringUtils.hasText(search)) {
            search = "%" + StringUtils.trimWhitespace(search) + "%";
            return ResponseEntity.ok(deviceRepository.findAllByProducerLikeOrConsumer_nicknameLikeOrConsumer_phoneLike(pageable, search, search, search));
        }
        return ResponseEntity.ok(deviceRepository.findAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Device> read(@PathVariable String id) {
        return ResponseEntity.ok(get(id));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Device> update(@Valid @RequestBody Device device) {
        Device old = get(device.getId());
        old.setSerialNumber(device.getSerialNumber());
        old.setSerialNumber1(device.getSerialNumber1());
        old.setSerialNumber2(device.getSerialNumber2());
        old.setType(device.getType());
        old.setProducer(device.getProducer());
        old.setDescription(device.getDescription());

        setReferenceProperties(device);
        //如果变更收货方则需要解除原有绑定
        if (StringUtils.hasText(old.getConsumerId()) && !old.getConsumerId().equals(device.getConsumerId())) {
            machineRepository.findByTriColorLED(old).ifPresent(machine -> {
                machine.setTriColorLED(null);
                machineRepository.save(machine);
            });
        }
        old.setConsumer(device.getConsumer());
        return ResponseEntity.ok(deviceRepository.save(old));
    }

    private final DeviceRepository deviceRepository;
    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
}
