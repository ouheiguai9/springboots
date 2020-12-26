package com.byakuya.boot.factory.component.menu;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by ganzl on 2020/12/18.
 */
public interface MenuRepository extends PagingAndSortingRepository<Menu, String> {
    @EntityGraph("Menu.Graph")
    @Query("select m from Menu m order by m.ordering desc")
    List<Menu> findAll();
}
