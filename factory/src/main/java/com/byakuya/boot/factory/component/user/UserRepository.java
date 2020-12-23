package com.byakuya.boot.factory.component.user;

import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph("User.Graph")
    Optional<User> findUserByIdOrUsernameOrPhoneOrEmail(String id, String username, String phone, String email);
}
