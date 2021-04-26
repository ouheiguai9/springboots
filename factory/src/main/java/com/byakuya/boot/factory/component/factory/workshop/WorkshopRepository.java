package com.byakuya.boot.factory.component.factory.workshop;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ganzl on 2021/4/26.
 */
public interface WorkshopRepository extends PagingAndSortingRepository<Workshop, String> {
    List<Workshop> findAllByCreatedBy_idOrderByNameAsc(String id);

    Optional<Workshop> findByIdAndCreatedBy_id(String id, String userId);
}
