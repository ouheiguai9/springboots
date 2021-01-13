package com.byakuya.boot.factory.component.device.log;

import com.byakuya.boot.factory.component.device.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by ganzl on 2021/1/5.
 */
public interface TriColorLedLogRepository extends CrudRepository<TriColorLedLog, String> {
    @Query("select new TriColorLedLog(log.device, log.status, sum(log.duration)) from TriColorLedLog log where log.status <> TriColorLedLog.Status.NONE and log.device in ?1 and log.time between ?2 and ?3 group by log.status, log.device")
    List<TriColorLedLog> findStatusRank(Iterable<Device> devices, LocalDateTime startTime, LocalDateTime endTime);

    Optional<TriColorLedLog> findFirstByDevice_IdOrderByTimeDesc(String deviceId);
}
