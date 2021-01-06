package com.byakuya.boot.factory.component.factory.machine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Created by ganzl on 2020/12/29.
 */
public interface MachineRepository extends PagingAndSortingRepository<Machine, String> {
    @EntityGraph("Machine.List")
    Page<Machine> findAllByCreatedBy_id(Pageable pageable, String id);

    Optional<Machine> findByIdAndCreatedBy_id(String id, String userId);
}
