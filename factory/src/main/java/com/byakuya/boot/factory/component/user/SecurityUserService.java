package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.config.property.SecurityProperties;
import com.byakuya.boot.factory.exception.CustomizedException;
import com.byakuya.boot.factory.exception.RecordNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by ganzl on 2020/11/30.
 */
@Service
public class SecurityUserService {
    public SecurityUserService(SecurityProperties securityProperties, SecurityUserRepository securityUserRepository) {
        this.securityProperties = securityProperties;
        this.securityUserRepository = securityUserRepository;
    }

    /**
     * 修改密码
     *
     * @param username    用户id
     * @param oldPassword 原始密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        SecurityUser securityUser = securityUserRepository.findByUsername(username).orElseThrow(() -> new RecordNotExistsException(username));
        if (!passwordEncoder.matches(oldPassword, securityUser.getPassword())) {
            throw new CustomizedException("ERR-90001");
        }
        securityUser.setPassword(passwordEncoder.encode(newPassword));
        securityUser.setLastPasswordModifiedDate(LocalDateTime.now());
        securityUserRepository.save(securityUser);
        return true;
    }

    /**
     * 通过用户唯一身份标识加载用户
     *
     * @param identifier 用户唯一标识(ID,用户名,邮箱,手机号码)
     * @return 用户
     */
    public Optional<SecurityUser> loadUser(String identifier) {
        return securityUserRepository.findUserByIdOrUsernameOrPhoneOrEmail(identifier, identifier, identifier, identifier);
    }

    /**
     * 修改个人详细信息,不可修改密码
     *
     * @param securityUser 修改用户信息
     * @return 更新后用户信息
     */
    public SecurityUser modifyUserDetail(SecurityUser securityUser) {
        SecurityUser old = get(securityUser.getId());
        old.setDetail(securityUser.getDetail());
        return securityUserRepository.save(old);
    }

    SecurityUser get(String id) {
        return securityUserRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id));
    }

    /**
     * 查询用户列表
     *
     * @param pageable 分页参数
     * @param search   过滤条件
     * @return 分页用户列表
     */
    Page<SecurityUser> queryList(Pageable pageable, String search) {
        return securityUserRepository.findAll(pageable);
    }

    /**
     * 新用户注册
     *
     * @param securityUser 新用户
     * @return 新用户
     */
    public SecurityUser regist(SecurityUser securityUser) {
        if (!StringUtils.hasText(securityUser.getPassword())) {
            securityUser.setPassword(securityProperties.getNewUserDefaultPassword());
        }
        securityUser.setPassword(passwordEncoder.encode(securityUser.getPassword()));
        return securityUserRepository.save(securityUser);
    }

    @Autowired(required = false)
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private final SecurityProperties securityProperties;
    private final SecurityUserRepository securityUserRepository;
    private PasswordEncoder passwordEncoder;
}
