package com.byakuya.boot.factory.component.device;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/26.
 */
public interface DeviceRepository extends PagingAndSortingRepository<Device, String> {
    @EntityGraph("Device.List")
    Page<Device> findAll(Pageable pageable);

    @Query("select d from Device d inner join d.consumer u where not exists (select 1 from Machine m where m.triColorLED=d) and u.id=?1 and d.type=?2")
    List<Device> findAllByConsumer_idAndTypeAndLockedFalse(String userId, Device.DeviceType type);

    @EntityGraph("Device.List")
    Page<Device> findAllByProducerLikeOrConsumer_nicknameLikeOrSerialNumberLike(Pageable pageable, String producer, String nickname, String serialNumber);

    Optional<Device> findByIdAndConsumer_idAndType(String id, String userId, Device.DeviceType type);

    Optional<Device> findDeviceBySerialNumberAndSerialNumber1AndType(String number, String number1, Device.DeviceType type);
}
