package com.byakuya.boot.factory.component.device;

import com.byakuya.boot.factory.config.AuthRestAPIController;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Created by ganzl on 2020/12/26.
 */
@AuthRestAPIController(path = {"devices"})
@Validated
public class DeviceController {
    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @PostMapping
    public ResponseEntity<Device> create(@Valid @RequestBody Device device) {
        return ResponseEntity.ok(deviceRepository.save(device));
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
    public ResponseEntity<Page<Device>> read(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(deviceRepository.findAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Device> read(@PathVariable String id) {
        return ResponseEntity.ok(get(id));
    }

    @PutMapping
    public ResponseEntity<Device> update(@Valid @RequestBody Device device) {
        Device old = get(device.getId());
        old.setSerialNumber(device.getSerialNumber());
        old.setSerialNumber1(device.getSerialNumber1());
        old.setSerialNumber2(device.getSerialNumber2());
        old.setType(device.getType());
        old.setProducer(device.getProducer());
        old.setConsumer(device.getConsumer());
        old.setDescription(device.getDescription());
        return ResponseEntity.ok(deviceRepository.save(old));
    }

    private final DeviceRepository deviceRepository;
}
