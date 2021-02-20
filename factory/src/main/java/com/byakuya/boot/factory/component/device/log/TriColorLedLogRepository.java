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
    List<TriColorLedLog> findAllByDevice_IdAndTimeBetweenOrderByTimeAsc(String deviceId, LocalDateTime start, LocalDateTime end);

    Optional<TriColorLedLog> findFirstByDevice_IdOrderByTimeDesc(String deviceId);

    @Query("select new TriColorLedLog(log.device, log.status, sum(log.duration)) from TriColorLedLog log where log.status <> 'NONE' and log.device in ?1 and log.time between ?2 and ?3 group by log.status, log.device")
    List<TriColorLedLog> findStatusRank(Iterable<Device> devices, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select new TriColorLedLog(log.device, log.status, count(log.duration)) from TriColorLedLog log where log.status = 'GREEN' and log.device in ?1 and log.time between ?2 and ?3 group by log.status, log.device")
    List<TriColorLedLog> findGreenCount(Iterable<Device> devices, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select new TriColorLedLog(log.device, log.status, count(log.duration)) from TriColorLedLog log where log.status = ?1 and log.device in ?2 and log.time between ?3 and ?4 and log.duration > ?5 group by log.status, log.device")
    List<TriColorLedLog> findStatusOvertimeCount(Iterable<Device> devices, TriColorLedLog.Status status, LocalDateTime startTime, LocalDateTime endTime, Long threshold);
}
