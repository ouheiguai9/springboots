package com.byakuya.boot.factory.component.device;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ganzl on 2020/12/26.
 */
public interface DeviceRepository extends PagingAndSortingRepository<Device, String> {
}
