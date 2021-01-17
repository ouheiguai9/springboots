package com.byakuya.boot.factory.component.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Created by ganzl on 2020/3/11.
 */
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    @EntityGraph("User.List")
    Page<User> findAll(Pageable pageable);

    Page<User> findAllByLockedFalse(Pageable pageable);

    @Query("select u from User u where (u.username like ?1 or u.nickname like ?2 or u.phone like ?3) and u.locked=false")
    Page<User> findAllSimple(Pageable pageable, String username, String nickname, String phone);

    Optional<User> findByUsername(String username);

    @Query("select m.id from User u,in(u.menuSet) m where u.id=?1")
    Iterable<String> findUserAllMenuId(String id);

    @EntityGraph("User.Login")
    Optional<User> findUserByIdOrUsernameOrPhone(String id, String username, String phone);

    @EntityGraph("User.List")
    Page<User> queryAllByUsernameLikeOrPhoneLike(Pageable pageable, String username, String phone);
}
