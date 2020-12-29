package com.byakuya.boot.factory.component.device;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Created by ganzl on 2020/12/26.
 */
public interface DeviceRepository extends PagingAndSortingRepository<Device, String> {
    Optional<Device> findByIdAndConsumer_idAndType(String id, String userId, Device.DeviceType type);
}
