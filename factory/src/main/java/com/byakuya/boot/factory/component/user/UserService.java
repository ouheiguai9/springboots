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
public class UserService {
    public UserService(SecurityProperties securityProperties, UserRepository userRepository) {
        this.securityProperties = securityProperties;
        this.userRepository = userRepository;
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
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RecordNotExistsException(username));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new CustomizedException("ERR-90001");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPasswordModifiedDate(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    /**
     * 通过用户唯一身份标识加载用户
     *
     * @param identifier 用户唯一标识(ID,用户名,邮箱,手机号码)
     * @return 用户
     */
    public Optional<User> loadUser(String identifier) {
        return userRepository.findUserByIdOrUsernameOrPhoneOrEmail(identifier, identifier, identifier, identifier);
    }

    /**
     * 修改个人详细信息,不可修改密码
     *
     * @param user 修改用户信息
     * @return 更新后用户信息
     */
    public User modifyUserDetail(User user) {
        User old = get(user.getId());
        old.setAddress(user.getAddress());
        old.setAvatar(user.getAvatar());
        old.setNickname(user.getNickname());
        old.setSex(user.isSex());
        return userRepository.save(old);
    }

    User get(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id));
    }

    /**
     * 查询用户列表
     *
     * @param pageable 分页参数
     * @param search   过滤条件
     * @return 分页用户列表
     */
    Page<User> queryList(Pageable pageable, String search) {
        return userRepository.findAll(pageable);
    }

    /**
     * 新用户注册
     *
     * @param user 新用户
     * @return 新用户
     */
    public User regist(User user) {
        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(securityProperties.getNewUserDefaultPassword());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Autowired(required = false)
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
}
