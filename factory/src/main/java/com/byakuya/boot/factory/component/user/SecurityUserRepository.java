package com.byakuya.boot.factory.component.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Created by ganzl on 2020/3/11.
 */
public interface SecurityUserRepository extends PagingAndSortingRepository<SecurityUser, String> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    Optional<SecurityUser> findByUsername(String username);

    Optional<SecurityUser> findUserByIdOrUsernameOrPhoneOrEmail(String id, String username, String phone, String email);
}
