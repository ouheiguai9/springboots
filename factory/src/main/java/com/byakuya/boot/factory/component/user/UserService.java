package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.component.menu.Menu;
import com.byakuya.boot.factory.component.menu.MenuRepository;
import com.byakuya.boot.factory.component.role.Role;
import com.byakuya.boot.factory.component.role.RoleRepository;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by ganzl on 2020/11/30.
 */
@Service
public class UserService {
    public UserService(MenuRepository menuRepository, RoleRepository roleRepository, SecurityProperties securityProperties, UserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
        this.securityProperties = securityProperties;
        this.userRepository = userRepository;
    }

    public User authorize(String id, String menuIdStr) {
        User old = get(id);
        Set<Menu> menuSet = new HashSet<>();
        if (StringUtils.hasText(menuIdStr)) {
            menuRepository.findAllById(Arrays.asList(menuIdStr.split(","))).forEach(menuSet::add);
        }
        old.setMenuSet(menuSet);
        return userRepository.save(old);
    }

    public User get(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RecordNotExistsException(id));
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
     * 修改全部属性
     *
     * @param user 修改用户信息
     * @return 更新后用户信息
     */
    User modifyAll(User user) {
        User old = get(user.getId());
        old.setUsername(user.getUsername());
        old.setPhone(user.getPhone());
        old.setEmail(user.getEmail());
        old.setBeginValidPeriod(user.getBeginValidPeriod());
        old.setEndValidPeriod(user.getEndValidPeriod());
        old.setLocked(user.isLocked());
        if (StringUtils.hasText(user.getPassword())) {
            old.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        String roleIdStr = user.getRoleIdStr();
        if (StringUtils.hasText(roleIdStr)) {
            Set<Role> roleSet = new HashSet<>();
            roleRepository.findAllById(Arrays.asList(roleIdStr.split(","))).forEach(roleSet::add);
            if (!roleSet.isEmpty()) {
                old.setRoleSet(roleSet);
            }
        } else {
            old.setRoleSet(null);
        }

        old.setAddress(user.getAddress());
        old.setAvatar(user.getAvatar());
        old.setNickname(user.getNickname());
        old.setSex(user.isSex());
        return userRepository.save(old);
    }

    /**
     * 修改用户锁定状态
     *
     * @param id     id
     * @param locked 是否锁定
     * @return 对象
     */
    User modifyLocked(String id, boolean locked) {
        User old = get(id);
        old.setLocked(locked);
        return userRepository.save(old);
    }

    /**
     * 修改特定属性
     *
     * @param user 修改用户信息
     * @return 更新后用户信息
     */
    public User modifyPart(User user) {
        User old = get(user.getId());
        old.setAddress(user.getAddress());
        old.setAvatar(user.getAvatar());
        old.setNickname(user.getNickname());
        old.setSex(user.isSex());
        return userRepository.save(old);
    }

    /**
     * 查询用户列表
     *
     * @param pageable 分页参数
     * @param search   过滤条件
     * @return 分页用户列表
     */
    Page<User> queryList(Pageable pageable, String search) {
        if (StringUtils.hasText(search)) {
            search = "%" + StringUtils.trimWhitespace(search) + "%";
            return userRepository.queryAllByUsernameLikeOrPhoneLike(pageable, search, search);
        }
        return userRepository.findAll(pageable);
    }

    /**
     * 查询用户列表
     *
     * @param pageable 分页参数
     * @param search   过滤条件
     * @return 分页用户列表
     */
    Page<User> queryListSimple(Pageable pageable, String search) {
        if (StringUtils.hasText(search)) {
            search = "%" + StringUtils.trimWhitespace(search) + "%";
            return userRepository.findAllSimple(pageable, search, search, search);
        }
        return userRepository.findAllByLockedFalse(pageable);
    }

    /**
     * 查询用户所有授权菜单id
     *
     * @param id 用户id
     * @return 所有授权菜单id
     */
    Iterable<String> queryUserAllMenuId(String id) {
        return userRepository.findUserAllMenuId(id);
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
        String roleIdStr = user.getRoleIdStr();
        if (StringUtils.hasText(roleIdStr)) {
            Set<Role> roleSet = new HashSet<>();
            roleRepository.findAllById(Arrays.asList(roleIdStr.split(","))).forEach(roleSet::add);
            if (!roleSet.isEmpty()) {
                user.setRoleSet(roleSet);
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Autowired(required = false)
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;
    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
}
