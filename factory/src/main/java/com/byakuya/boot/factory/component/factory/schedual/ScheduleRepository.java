package com.byakuya.boot.factory.component.factory.schedual;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Created by ganzl on 2020/12/28.
 */
public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, String> {
    Iterable<Schedule> findAllByCreatedBy_idOrderByStartTimeAsc(String id);

    Optional<Schedule> findByIdAndCreatedBy_id(String id, String userId);
}
