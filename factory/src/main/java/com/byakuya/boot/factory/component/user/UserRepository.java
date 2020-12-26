package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.component.menu.Menu;
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

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @EntityGraph("User.Login")
    Optional<User> findUserByIdOrUsernameOrPhoneOrEmail(String id, String username, String phone, String email);

    @EntityGraph("User.List")
    Page<User> findAll(Pageable pageable);

    @EntityGraph("User.List")
    Page<User> queryAllByUsernameLikeOrPhoneLike(Pageable pageable, String username, String phone);

//    @Query("select m.id from User u join fetch u.roleSet,u.menuSet left join Menu m on u.me where u.id=?1")
//    Iterable<Menu> findUserWithRoleAllMenu(String id);

    @Query("select m.id from User u,in(u.menuSet) m where u.id=?1")
    Iterable<String> findUserAllMenuId(String id);
}
