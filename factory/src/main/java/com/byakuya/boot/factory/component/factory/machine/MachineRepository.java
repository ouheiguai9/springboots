package com.byakuya.boot.factory.component.factory.machine;

import com.byakuya.boot.factory.component.device.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ganzl on 2020/12/29.
 */
public interface MachineRepository extends PagingAndSortingRepository<Machine, String> {
    @EntityGraph("Machine.List")
    @Query("select m from Machine m left join m.createdBy u left join m.triColorLED t where m.triColorLED is not null and u.id=?1 and t.locked='0' order by m.ordering asc")
    List<Machine> findAllBindTriColorLED(String id);

    @EntityGraph("Machine.List")
    Page<Machine> findAllByCreatedBy_id(Pageable pageable, String id);

    Optional<Machine> findByIdAndCreatedBy_id(String id, String userId);

    Optional<Machine> findByTriColorLED(Device device);
}
