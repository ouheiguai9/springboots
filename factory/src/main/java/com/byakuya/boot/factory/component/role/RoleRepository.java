package com.byakuya.boot.factory.component.role;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ganzl on 2020/12/18.
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, String> {
    @Query("select m.id from Role r,in(r.menuSet) m where r.id=?1")
    Iterable<String> findRoleAllMenuId(String id);
}
