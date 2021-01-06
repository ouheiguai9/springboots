package com.byakuya.boot.factory.component.device.log;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by ganzl on 2021/1/5.
 */
public interface TriColorLedLogRepository extends CrudRepository<TriColorLedLog, String> {
    Optional<TriColorLedLog> findFirstByDevice_IdOrderByTimeDesc(String deviceId);
}
