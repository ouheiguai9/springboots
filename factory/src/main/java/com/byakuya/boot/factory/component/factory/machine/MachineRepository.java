package com.byakuya.boot.factory.component.factory.machine;

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
    Page<Machine> findAllByCreatedBy_id(Pageable pageable, String id);

    @EntityGraph("Machine.List")
    @Query("select m from Machine m left join m.createdBy u on u.id=?1 inner join m.triColorLED t on t.locked='0' where m.triColorLED is not null order by m.createdDate asc")
    List<Machine> findAllBindTriColorLED(String id);

    Optional<Machine> findByIdAndCreatedBy_id(String id, String userId);
}
